package org.metadatacenter.util.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.metadatacenter.constant.CedarConstants;

import java.time.LocalDateTime;

public final class JsonMapper {

  private JsonMapper() {
  }

  public static final ObjectMapper MAPPER;
  public static final ObjectMapper PRETTY_MAPPER;

  static {
    JavaTimeModule javaTimeModule = new JavaTimeModule();
    javaTimeModule.addSerializer(LocalDateTime.class,
        new LocalDateTimeSerializer(CedarConstants.xsdDateTimeFormatter));
    javaTimeModule.addDeserializer(LocalDateTime.class,
        new LocalDateTimeDeserializer(CedarConstants.xsdDateTimeFormatter));

    MAPPER = new ObjectMapper();
    MAPPER.registerModule(new JavaTimeModule());
    MAPPER.registerModule(javaTimeModule);
    MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    // Do not use, infinite loop MAPPER.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);

    PRETTY_MAPPER = new ObjectMapper();
    PRETTY_MAPPER.registerModule(new JavaTimeModule());
    PRETTY_MAPPER.registerModule(javaTimeModule);
    PRETTY_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    PRETTY_MAPPER.configure(SerializationFeature.INDENT_OUTPUT, true);
    // Do not use, infinite loop PRETTY_MAPPER.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
  }
}
