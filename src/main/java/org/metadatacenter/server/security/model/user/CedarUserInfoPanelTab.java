package org.metadatacenter.server.security.model.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CedarUserInfoPanelTab {
  INFO("info"),
  VERSION("version"),
  CATEGORY("category"),
  SHARE("share");

  private final String value;

  CedarUserInfoPanelTab(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @JsonCreator
  public static CedarUserInfoPanelTab forValue(String value) {
    for (CedarUserInfoPanelTab t : values()) {
      if (t.getValue().equals(value)) {
        return t;
      }
    }
    return null;
  }

}
