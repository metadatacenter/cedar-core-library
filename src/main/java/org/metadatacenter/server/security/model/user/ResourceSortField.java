package org.metadatacenter.server.security.model.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** The property the Workspace sorts a listing by, spelled as the listing endpoints' sort parameter. */
public enum ResourceSortField {

  NAME("name"),
  CREATED_ON("createdOnTS"),
  LAST_UPDATED_ON("lastUpdatedOnTS");

  private final String value;

  ResourceSortField(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @JsonCreator
  public static ResourceSortField forValue(String value) {
    for (ResourceSortField f : values()) {
      if (f.getValue().equals(value)) {
        return f;
      }
    }
    return null;
  }
}
