package org.metadatacenter.server.security.model.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.ArrayList;
import java.util.List;

public enum ResourceVersionFilter {

  LATEST("latest"),
  LATEST_BY_STATUS("latest-by-status"),
  ALL("all");

  private final String value;
  private static final List<String> VALID_VALUES;

  static {
    VALID_VALUES = new ArrayList<>();
    for (ResourceVersionFilter t : values()) {
      VALID_VALUES.add(t.getValue());
    }
  }

  ResourceVersionFilter(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @JsonCreator
  public static ResourceVersionFilter forValue(String value) {
    for (ResourceVersionFilter t : values()) {
      if (t.getValue().equals(value)) {
        return t;
      }
    }
    return null;
  }

  public static List<String> getValidValues() {
    return VALID_VALUES;
  }
}