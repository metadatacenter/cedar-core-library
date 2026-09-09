package org.metadatacenter.server.security.model.user;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.metadatacenter.util.json.JsonMapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public class CedarUserApiKeyMapTest {

  private static final String ONE_KEY =
      "{\"secretA\":{\"id\":\"idA\",\"key\":\"secretA\",\"serviceName\":\"svc\",\"enabled\":true}}";

  /** The graph stores the map as a JSON string, which Jackson passes to the String constructor. */
  private static CedarUserApiKeyMap asStoredInTheGraph(String stored) throws Exception {
    return JsonMapper.MAPPER.readValue(JsonMapper.MAPPER.writeValueAsString(stored), CedarUserApiKeyMap.class);
  }

  @Test
  public void aStoredMapIsReadThroughTheStringConstructor() throws Exception {
    CedarUserApiKeyMap map = asStoredInTheGraph(ONE_KEY);

    Assertions.assertEquals(1, map.size());
    Assertions.assertEquals("secretA", map.get("secretA").getKey());
    Assertions.assertFalse(map.isUnreadable());
  }

  @Test
  public void aFieldFromAnotherReleaseDoesNotMakeTheKeysUnreadable() throws Exception {
    String fromALaterRelease =
        "{\"secretA\":{\"id\":\"idA\",\"key\":\"secretA\",\"fieldAddedLater\":42}}";

    CedarUserApiKeyMap map = asStoredInTheGraph(fromALaterRelease);

    Assertions.assertFalse(map.isUnreadable(), "an unknown property must not cost the user their keys");
    Assertions.assertEquals(1, map.size());
    Assertions.assertEquals("secretA", map.get("secretA").getKey());
  }

  @Test
  public void storedKeyToleranceComesFromTheSelectedMapper() throws Exception {
    String fromALaterRelease =
        "{\"secretA\":{\"id\":\"idA\",\"key\":\"secretA\",\"fieldAddedLater\":42}}";

    Assertions.assertThrows(UnrecognizedPropertyException.class,
        () -> JsonMapper.STRICT_MAPPER.readValue(fromALaterRelease, CedarUserApiKeyMap.class));
    CedarUserApiKeyMap map = JsonMapper.TOLERANT_MAPPER.readValue(
        fromALaterRelease, CedarUserApiKeyMap.class);

    Assertions.assertEquals("secretA", map.get("secretA").getKey());
  }

  /** Earlier releases stored the creation date as Jackson's default local date-time, with no offset. */
  @Test
  public void aCreationDateStoredWithoutAnOffsetIsReadInTheSystemZone() throws Exception {
    String fromAnEarlierRelease =
        "{\"secretA\":{\"id\":\"idA\",\"key\":\"secretA\",\"creationDate\":\"2024-03-01T09:15:00.25\",\"enabled\":true}}";

    CedarUserApiKeyMap map = asStoredInTheGraph(fromAnEarlierRelease);

    Assertions.assertFalse(map.isUnreadable(), "an offset-less date must not cost the user their keys");
    OffsetDateTime expected = LocalDateTime.of(2024, 3, 1, 9, 15, 0, 250_000_000)
        .atZone(ZoneId.systemDefault()).toOffsetDateTime();
    Assertions.assertEquals(expected, map.get("secretA").getCreationDate());
  }

  @Test
  public void aCorruptMapIsMarkedUnreadableRatherThanEmpty() throws Exception {
    CedarUserApiKeyMap map = asStoredInTheGraph("{\"secretA\":{\"id\":");

    Assertions.assertTrue(map.isUnreadable());
    Assertions.assertTrue(map.isEmpty(), "nothing could be recovered, so the map is also empty");
  }

  @Test
  public void aUserWithNoKeysIsNotMarkedUnreadable() throws Exception {
    CedarUserApiKeyMap map = asStoredInTheGraph("{}");

    Assertions.assertFalse(map.isUnreadable());
    Assertions.assertTrue(map.isEmpty());
  }

  @Test
  public void theUnreadableFlagIsNotSerializedIntoTheStoredMap() throws Exception {
    CedarUserApiKeyMap map = asStoredInTheGraph("{\"secretA\":{\"id\":");

    String written = JsonMapper.MAPPER.writeValueAsString(map);

    Assertions.assertFalse(written.contains("unreadable"), "the flag is state about the read, not a stored key");
  }
}
