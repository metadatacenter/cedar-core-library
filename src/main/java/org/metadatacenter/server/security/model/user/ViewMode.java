package org.metadatacenter.server.security.model.user;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ViewMode {

  GRID("grid"), LIST("list");

  private final String value;

  ViewMode(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  public static ViewMode forValue(String v) {
    for (ViewMode vm : ViewMode.values()) {
      if (vm.getValue().equals(v)) {
        return vm;
      }
    }
    return null;
  }
}
