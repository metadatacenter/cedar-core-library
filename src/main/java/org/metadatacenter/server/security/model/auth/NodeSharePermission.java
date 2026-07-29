package org.metadatacenter.server.security.model.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionGroupPermissionPair;

import java.util.Objects;

public enum NodeSharePermission {

  READ(Type.READ),
  WRITE(Type.WRITE),
  NONE(Type.NONE);

  public static NodeSharePermission fromGroupPermission(ResourcePermissionGroupPermissionPair groupPermission) {
    if (groupPermission != null) {
      return forValue(groupPermission.getPermission().getValue());
    } else {
      return NONE;
    }
  }

  public static class Type {
    public static final String READ = "read";
    public static final String WRITE = "write";
    public static final String NONE = null;
  }

  private final String value;

  NodeSharePermission(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @JsonCreator
  public static NodeSharePermission forValue(String type) {
    for (NodeSharePermission t : values()) {
      // Objects.equals is null-safe both ways: NONE's value is deliberately null, so a plain
      // t.getValue().equals(type) throws when the loop reaches NONE. A null or unknown input now maps
      // to NONE (matching NONE's null value, or falling through to the return below) instead of NPEing.
      if (Objects.equals(t.getValue(), type)) {
        return t;
      }
    }
    return NONE;
  }

  public boolean equals(NodeSharePermission other) {
    return other != null &&
        (
            (this.value == null && other.value == null) || (this.value != null && this.value.equals(other.value))
        );
  }
}
