package org.metadatacenter.util.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.metadatacenter.constant.CedarConstants;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;

/**
 * Builds the estate's {@link JavaTimeModule}. It is the stock module, except that an {@link OffsetDateTime}
 * is written with {@link CedarConstants#xsdDateTimeFormatter}, CEDAR's {@code xsd:dateTime} shape of whole
 * seconds and a UTC offset, the form every {@code pav:createdOn} in the estate carries. An offset needs a
 * value that has one, which is why the customised type is {@code OffsetDateTime} and not
 * {@code LocalDateTime}.
 * <p>
 * Register the module this builds on its own. Jackson ignores a second module with the same type id,
 * so a stock {@code JavaTimeModule} registered first would silently shadow the customised one.
 * <p>
 * Reading is lenient about the offset, because stored values predate it: user records written before
 * the API key's creation date carried an offset hold a plain local date-time, which is read in the
 * system zone. The colon-less offset ({@code -0800}) that older CEDAR output used is accepted as well.
 */
public final class CedarJavaTimeModule {

  private static final DateTimeFormatter LENIENT_XSD_DATE_TIME = new DateTimeFormatterBuilder()
      .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
      .optionalStart().appendOffset("+HH:MM", "Z").optionalEnd()
      .optionalStart().appendOffset("+HHMM", "Z").optionalEnd()
      .toFormatter();

  private CedarJavaTimeModule() {
  }

  public static JavaTimeModule create() {
    JavaTimeModule module = new JavaTimeModule();
    module.addSerializer(OffsetDateTime.class, new XsdDateTimeSerializer());
    module.addDeserializer(OffsetDateTime.class, new XsdDateTimeDeserializer());
    return module;
  }

  static OffsetDateTime parseXsdDateTime(String text) {
    TemporalAccessor parsed = LENIENT_XSD_DATE_TIME.parse(text);
    ZoneOffset offset = parsed.query(TemporalQueries.offset());
    if (offset != null) {
      return OffsetDateTime.from(parsed);
    }
    return LocalDateTime.from(parsed).atZone(ZoneId.systemDefault()).toOffsetDateTime();
  }

  private static final class XsdDateTimeSerializer extends StdSerializer<OffsetDateTime> {

    XsdDateTimeSerializer() {
      super(OffsetDateTime.class);
    }

    @Override
    public void serialize(OffsetDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
      gen.writeString(CedarConstants.xsdDateTimeFormatter.format(value));
    }
  }

  private static final class XsdDateTimeDeserializer extends StdDeserializer<OffsetDateTime> {

    XsdDateTimeDeserializer() {
      super(OffsetDateTime.class);
    }

    @Override
    public OffsetDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      if (!p.hasToken(JsonToken.VALUE_STRING)) {
        return (OffsetDateTime) ctxt.handleUnexpectedToken(OffsetDateTime.class, p);
      }
      String text = p.getText().trim();
      if (text.isEmpty()) {
        return null;
      }
      try {
        return parseXsdDateTime(text);
      } catch (DateTimeParseException e) {
        throw ctxt.weirdStringException(text, OffsetDateTime.class, e.getMessage());
      }
    }
  }
}
