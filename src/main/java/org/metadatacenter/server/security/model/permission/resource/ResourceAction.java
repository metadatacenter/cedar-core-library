package org.metadatacenter.server.security.model.permission.resource;

import com.fasterxml.jackson.annotation.JsonValue;

/** An operation currently available after authority and resource context are both considered. */
public enum ResourceAction {
  COPY_FROM_RESOURCE("copyFromResource"),
  CREATE_DRAFT("createDraft"),
  PUBLISH("publish"),
  SUBMIT("submit"),
  POPULATE("populate"),
  ENABLE_OPENVIEW("enableOpenView"),
  DISABLE_OPENVIEW("disableOpenView");

  private final String value;

  ResourceAction(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }
}
