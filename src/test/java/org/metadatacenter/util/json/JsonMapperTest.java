package org.metadatacenter.util.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Pattern;

public class JsonMapperTest {

  /** Whole seconds remain valid; fractional seconds are preserved when present. */
  private static final Pattern XSD_DATE_TIME =
      Pattern.compile("\"\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d{1,9})?(Z|[+-]\\d{2}:\\d{2})\"");

  private static final OffsetDateTime WITH_NANOS =
      OffsetDateTime.of(2026, 9, 2, 10, 30, 15, 123_456_789, ZoneOffset.ofHours(-7));

  private static final List<ObjectMapper> MAPPERS =
      List.of(JsonMapper.STRICT_MAPPER, JsonMapper.TOLERANT_MAPPER, JsonMapper.PRETTY_MAPPER);

  @Test
  public void rawDocumentsRejectAmbiguityAndKeepExactNumbers() throws Exception {
    for (ObjectMapper mapper : MAPPERS) {
      Assertions.assertThrows(java.io.IOException.class, () -> mapper.readTree("{\"x\":1,\"x\":2}"));
      Assertions.assertThrows(java.io.IOException.class, () -> mapper.readTree("{\"x\":1} {}"));
      var tree = mapper.readTree("{\"integer\":9223372036854775808,\"decimal\":0.10000000000000001}");
      Assertions.assertEquals("9223372036854775808", tree.get("integer").asText());
      Assertions.assertEquals("0.10000000000000001", tree.get("decimal").asText());
      Assertions.assertEquals(tree, mapper.readTree(mapper.writeValueAsString(tree)));
    }
  }

  @Test
  public void unknownPropertyPoliciesAreExplicit() throws Exception {
    String json = "{\"when\":\"2026-09-02T10:30:15Z\",\"futureField\":true}";

    Assertions.assertSame(JsonMapper.STRICT_MAPPER, JsonMapper.STRICT_MAPPER);
    Assertions.assertThrows(UnrecognizedPropertyException.class,
        () -> JsonMapper.STRICT_MAPPER.readValue(json, Holder.class));
    Assertions.assertThrows(UnrecognizedPropertyException.class,
        () -> JsonMapper.PRETTY_MAPPER.readValue(json, Holder.class));

    Holder read = JsonMapper.TOLERANT_MAPPER.readValue(json, Holder.class);
    Assertions.assertEquals(OffsetDateTime.parse("2026-09-02T10:30:15Z"), read.when);
  }

  /** A typed JSON round trip must not discard the precision carried by its timestamp. */
  @Test
  public void offsetDateTimeIsWrittenInCedarsXsdDateTimeShapeByBothMappers() throws Exception {
    for (ObjectMapper mapper : MAPPERS) {
      String written = mapper.writeValueAsString(WITH_NANOS);
      Assertions.assertTrue(XSD_DATE_TIME.matcher(written).matches(), written);
      OffsetDateTime read = mapper.readValue(written, OffsetDateTime.class);
      Assertions.assertTrue(read.isEqual(WITH_NANOS), written);
    }
  }

  @Test
  public void offsetDateTimeInsideAnObjectIsWrittenTheSameWay() throws Exception {
    String written = JsonMapper.STRICT_MAPPER.writeValueAsString(new Holder(WITH_NANOS));
    Assertions.assertTrue(written.matches("\\{\"when\":" + XSD_DATE_TIME.pattern() + "\\}"), written);
    Holder read = JsonMapper.STRICT_MAPPER.readValue(written, Holder.class);
    Assertions.assertTrue(read.when.isEqual(WITH_NANOS));
  }

  /** Records written before the creation date carried an offset hold a local date-time, read in the system zone. */
  @Test
  public void aStoredLocalDateTimeWithoutAnOffsetIsReadInTheSystemZone() throws Exception {
    OffsetDateTime expected = LocalDateTime.of(2024, 3, 1, 9, 15, 0, 250_000_000)
        .atZone(ZoneId.systemDefault()).toOffsetDateTime();
    Assertions.assertEquals(expected, read("2024-03-01T09:15:00.25"));
    Assertions.assertEquals(expected.truncatedTo(ChronoUnit.SECONDS), read("2024-03-01T09:15:00"));
  }

  @Test
  public void everyOffsetSpellingCedarHasEmittedIsAccepted() throws Exception {
    OffsetDateTime expected = OffsetDateTime.of(2024, 3, 1, 9, 15, 0, 0, ZoneOffset.ofHours(-8));
    Assertions.assertTrue(expected.isEqual(read("2024-03-01T09:15:00-08:00")));
    Assertions.assertTrue(expected.isEqual(read("2024-03-01T09:15:00-0800")));
    Assertions.assertTrue(expected.isEqual(read("2024-03-01T17:15:00Z")));
    Assertions.assertTrue(expected.isEqual(read("2024-03-01T09:15:00.000-08:00")));
  }

  @Test
  public void anUnreadableDateIsAFormatErrorNotASilentNull() {
    Assertions.assertThrows(InvalidFormatException.class, () -> read("yesterday"));
  }

  @Test
  public void theStockTypesOfTheModuleStillWork() throws Exception {
    LocalDateTime local = LocalDateTime.of(2026, 9, 2, 10, 30, 15);
    String written = JsonMapper.STRICT_MAPPER.writeValueAsString(local);
    Assertions.assertEquals("\"2026-09-02T10:30:15\"", written);
    Assertions.assertEquals(local, JsonMapper.STRICT_MAPPER.readValue(written, LocalDateTime.class));
  }

  private static OffsetDateTime read(String text) throws Exception {
    return JsonMapper.STRICT_MAPPER.readValue("\"" + text + "\"", OffsetDateTime.class);
  }

  public static final class Holder {
    public OffsetDateTime when;

    public Holder() {
    }

    Holder(OffsetDateTime when) {
      this.when = when;
    }
  }
}
