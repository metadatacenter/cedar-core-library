package org.metadatacenter.error;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CedarErrorReasonKey {

  NONE(null),

  NON_EMPTY_FOLDER("nonEmptyFolder"),
  USER_HOME_FOLDER("userHomeFolder"),
  SYSTEM_FOLDER("systemFolder"),
  TEMPLATE_REFERENCED_IN_INSTANCES("templateReferencedInInstances"),
  VALIDATION_ERROR("validationError");

  private final String value;

  CedarErrorReasonKey(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

}
