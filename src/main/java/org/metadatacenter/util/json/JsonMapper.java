package org.metadatacenter.util.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * The shared mappers. Typed reads select an explicit compatibility policy: strict reads reject
 * properties outside a closed contract, while tolerant reads ignore properties added by a newer
 * response or stored-record producer. Both still reject malformed JSON and invalid known values.
 *
 * <p>Each mapper registers the module {@link CedarJavaTimeModule#create()} builds exactly once:
 * Jackson drops a second module with the same type id, so registering a stock
 * {@code JavaTimeModule} alongside it would leave the customised one without effect.
 */
public final class JsonMapper {

  private JsonMapper() {
  }

  public static final ObjectMapper STRICT_MAPPER = create(true, false);
  public static final ObjectMapper TOLERANT_MAPPER = create(false, false);

  /**
   * Compatibility name for existing call sites. It remains strict; new typed reads should name
   * {@link #STRICT_MAPPER} or {@link #TOLERANT_MAPPER} according to their boundary.
   */
  public static final ObjectMapper MAPPER = STRICT_MAPPER;

  public static final ObjectMapper PRETTY_MAPPER = create(true, true);

  private static ObjectMapper create(boolean failOnUnknownProperties, boolean pretty) {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(CedarJavaTimeModule.create());
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknownProperties);
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    mapper.configure(SerializationFeature.INDENT_OUTPUT, pretty);
    // Do not disable FAIL_ON_SELF_REFERENCES: doing so creates an infinite loop.
    return mapper;
  }
}
