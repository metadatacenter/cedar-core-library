package org.metadatacenter.util.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * The shared mappers. Each registers the module {@link CedarJavaTimeModule#create()} builds exactly once: Jackson drops a second
 * module with the same type id, so registering a stock {@code JavaTimeModule} alongside it would leave
 * the customised one without effect.
 */
public final class JsonMapper {

  private JsonMapper() {
  }

  public static final ObjectMapper MAPPER;
  public static final ObjectMapper PRETTY_MAPPER;

  static {
    MAPPER = new ObjectMapper();
    MAPPER.registerModule(CedarJavaTimeModule.create());
    MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    // Do not use, infinite loop MAPPER.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);

    PRETTY_MAPPER = new ObjectMapper();
    PRETTY_MAPPER.registerModule(CedarJavaTimeModule.create());
    PRETTY_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    PRETTY_MAPPER.configure(SerializationFeature.INDENT_OUTPUT, true);
    // Do not use, infinite loop PRETTY_MAPPER.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
  }
}
