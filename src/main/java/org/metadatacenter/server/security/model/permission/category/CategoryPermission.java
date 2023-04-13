package org.metadatacenter.server.security.model.permission.category;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CategoryPermission {

  ATTACH(Type.ATTACH),
  WRITE(Type.WRITE);

  public static class Type {
    public static final String ATTACH = "attach";
    public static final String WRITE = "write";
  }

  private final String value;

  CategoryPermission(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  public static CategoryPermission forValue(String type) {
    for (CategoryPermission t : values()) {
      if (t.getValue().equals(type)) {
        return t;
      }
    }
    return null;
  }

  public boolean equals(CategoryPermission other) {
    return other != null &&
        (
            (this.value == null && other.value == null) || (this.value != null && this.value.equals(other.value))
        );
  }
}
