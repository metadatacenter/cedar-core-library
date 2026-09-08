package org.metadatacenter.server.security.model.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.metadatacenter.util.json.JsonMapper;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class CedarUserApiKeyTest {

  @Test
  public void legacyKeyGetsAStableNonSecretId() throws Exception {
    CedarUserApiKey key = new CedarUserApiKey();
    key.setKey("0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef");

    String id = key.getId();
    Assertions.assertTrue(id.startsWith("legacy-"));
    Assertions.assertFalse(id.contains(key.getKey()));

    CedarUserApiKey roundTripped = JsonMapper.MAPPER.readValue(
        JsonMapper.MAPPER.writeValueAsString(key), CedarUserApiKey.class);
    Assertions.assertEquals(id, roundTripped.getId());
  }

  /** The graph stores the key with whole seconds and an offset; nanoseconds do not survive, the instant does. */
  @Test
  public void creationDateSurvivesTheGraphRoundTrip() throws Exception {
    OffsetDateTime created = OffsetDateTime.of(2026, 9, 2, 10, 30, 15, 123_456_789, ZoneOffset.ofHours(-7));
    CedarUserApiKey key = new CedarUserApiKey();
    key.setKey("secret");
    key.setCreationDate(created);

    String written = JsonMapper.MAPPER.writeValueAsString(key);
    // The formatter renders the instant in the system zone, so only the shape is fixed: whole seconds and an offset.
    Assertions.assertTrue(written.matches(".*\"creationDate\":\"\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(Z|[+-]\\d{2}:\\d{2})\".*"), written);
    CedarUserApiKey roundTripped = JsonMapper.MAPPER.readValue(written, CedarUserApiKey.class);
    Assertions.assertTrue(roundTripped.getCreationDate().isEqual(created.truncatedTo(ChronoUnit.SECONDS)), written);
  }

  /**
   * REST responses go through Dropwizard's mapper, not the shared one. Dropwizard leaves Jackson's default
   * of writing dates as timestamps in place, so without the field's own format an OffsetDateTime would
   * reach the browser as a decimal epoch second. The profile page shows this field.
   */
  @Test
  public void aMapperThatWritesDatesAsTimestampsStillWritesTheCreationDateAsCedarsString() throws Exception {
    ObjectMapper likeDropwizard = new ObjectMapper().registerModule(new JavaTimeModule());
    CedarUserApiKey key = new CedarUserApiKey();
    key.setKey("secret");
    key.setCreationDate(OffsetDateTime.of(2026, 9, 2, 10, 30, 15, 123_456_789, ZoneOffset.ofHours(-7)));

    String written = likeDropwizard.writeValueAsString(key);

    Assertions.assertTrue(written.contains("\"creationDate\":\"2026-09-02T10:30:15-07:00\""), written);
    CedarUserApiKey read = likeDropwizard.readValue(written, CedarUserApiKey.class);
    Assertions.assertTrue(read.getCreationDate().isEqual(key.getCreationDate().truncatedTo(ChronoUnit.SECONDS)));
  }

  @Test
  public void rotatingAKeyDoesNotChangeItsId() {
    CedarUserApiKey key = new CedarUserApiKey();
    key.setKey("old-secret");
    String id = key.getId();

    key.setKey("new-secret");

    Assertions.assertEquals(id, key.getId());
  }

  @Test
  public void explicitIdIsPreserved() {
    CedarUserApiKey key = new CedarUserApiKey();
    String id = UUID.randomUUID().toString();
    key.setId(id);
    key.setKey("secret");

    Assertions.assertEquals(id, key.getId());
  }
}
