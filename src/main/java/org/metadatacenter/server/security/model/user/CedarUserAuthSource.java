package org.metadatacenter.server.security.model.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CedarUserAuthSource {
  TOKEN("token"),
  API_KEY("apiKey"),
  ANONYMOUS("anonymous");

  private final String value;

  CedarUserAuthSource(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @JsonCreator
  public static CedarUserAuthSource forValue(String value) {
    for (CedarUserAuthSource t : values()) {
      if (t.getValue().equals(value)) {
        return t;
      }
    }
    return null;
  }

}
