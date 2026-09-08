package org.metadatacenter.util.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
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

  /**
   * Whole seconds and a UTC offset, the shape of every {@code pav:createdOn} CEDAR emits. The formatter
   * renders the value in the system zone, and prints {@code Z} rather than {@code +00:00} when that zone is
   * UTC, as it is on the CI runners and on the production hosts.
   */
  private static final Pattern XSD_DATE_TIME =
      Pattern.compile("\"\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(Z|[+-]\\d{2}:\\d{2})\"");

  private static final OffsetDateTime WITH_NANOS =
      OffsetDateTime.of(2026, 9, 2, 10, 30, 15, 123_456_789, ZoneOffset.ofHours(-7));

  private static final List<ObjectMapper> MAPPERS = List.of(JsonMapper.MAPPER, JsonMapper.PRETTY_MAPPER);

  /**
   * The stock serializer writes ISO-8601 with nanoseconds. Only the customised module truncates to
   * seconds, so a value with nanoseconds tells the two apart: this fails if a stock JavaTimeModule is
   * registered before ours, which Jackson's duplicate-module rule turns into a silent no-op for ours.
   */
  @Test
  public void offsetDateTimeIsWrittenInCedarsXsdDateTimeShapeByBothMappers() throws Exception {
    for (ObjectMapper mapper : MAPPERS) {
      String written = mapper.writeValueAsString(WITH_NANOS);
      Assertions.assertTrue(XSD_DATE_TIME.matcher(written).matches(), written);
      OffsetDateTime read = mapper.readValue(written, OffsetDateTime.class);
      Assertions.assertTrue(read.isEqual(WITH_NANOS.truncatedTo(ChronoUnit.SECONDS)), written);
    }
  }

  @Test
  public void offsetDateTimeInsideAnObjectIsWrittenTheSameWay() throws Exception {
    String written = JsonMapper.MAPPER.writeValueAsString(new Holder(WITH_NANOS));
    Assertions.assertTrue(written.matches("\\{\"when\":" + XSD_DATE_TIME.pattern() + "\\}"), written);
    Holder read = JsonMapper.MAPPER.readValue(written, Holder.class);
    Assertions.assertTrue(read.when.isEqual(WITH_NANOS.truncatedTo(ChronoUnit.SECONDS)));
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
    String written = JsonMapper.MAPPER.writeValueAsString(local);
    Assertions.assertEquals("\"2026-09-02T10:30:15\"", written);
    Assertions.assertEquals(local, JsonMapper.MAPPER.readValue(written, LocalDateTime.class));
  }

  private static OffsetDateTime read(String text) throws Exception {
    return JsonMapper.MAPPER.readValue("\"" + text + "\"", OffsetDateTime.class);
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
