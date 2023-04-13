package org.metadatacenter.server.security.model.user;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SortDirection {

  ASC("asc"), DESC("desc");

  private final String value;

  SortDirection(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  public static SortDirection forValue(String v) {
    for (SortDirection sd : SortDirection.values()) {
      if (sd.getValue().equals(v)) {
        return sd;
      }
    }
    return null;
  }
}
