package org.metadatacenter.server.security.model.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CedarSuperRole {
  NORMAL("normal"),
  BUILT_IN_ADMIN("builtInAdmin");

  private final String value;

  CedarSuperRole(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @JsonCreator
  public static CedarSuperRole forValue(String value) {
    for (CedarSuperRole t : values()) {
      if (t.getValue().equals(value)) {
        return t;
      }
    }
    return null;
  }

}
