package org.metadatacenter.server.security.model.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The last-modified range the Workspace lists. Each named range ends today; a custom range carries
 * its own bounds.
 */
public enum ResourceModifiedFilter {

  TODAY("today"),
  WEEK("week"),
  MONTH("month"),
  YEAR("year"),
  LAST_YEAR("last-year"),
  CUSTOM("custom");

  private final String value;

  ResourceModifiedFilter(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @JsonCreator
  public static ResourceModifiedFilter forValue(String value) {
    for (ResourceModifiedFilter f : values()) {
      if (f.getValue().equals(value)) {
        return f;
      }
    }
    return null;
  }
}
