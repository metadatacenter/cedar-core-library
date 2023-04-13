package org.metadatacenter.server.security.model.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.ArrayList;
import java.util.List;

public enum ResourcePublicationStatusFilter {

  DRAFT("bibo:draft"),
  PUBLISHED("bibo:published"),
  ALL("all");

  private final String value;
  private static final List<String> VALID_VALUES;

  static {
    VALID_VALUES = new ArrayList<>();
    for (ResourcePublicationStatusFilter t : values()) {
      VALID_VALUES.add(t.getValue());
    }
  }

  ResourcePublicationStatusFilter(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @JsonCreator
  public static ResourcePublicationStatusFilter forValue(String value) {
    for (ResourcePublicationStatusFilter t : values()) {
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