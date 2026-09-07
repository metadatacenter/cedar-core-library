package org.metadatacenter.server.security.model.permission.category;

import com.fasterxml.jackson.annotation.JsonValue;

/** One operation governed by the category permission model. */
public enum CategoryCapability {
  READ_CATEGORY("readCategory"),
  ATTACH_CATEGORY("attachCategory"),
  DETACH_CATEGORY("detachCategory"),
  UPDATE_CATEGORY("updateCategory"),
  CREATE_CHILD_CATEGORY("createChildCategory"),
  DELETE_CATEGORY("deleteCategory"),
  MANAGE_GRANTS("manageGrants"),
  MOVE_CATEGORY("moveCategory"),
  TRANSFER_OWNERSHIP("transferOwnership");

  private final String value;

  CategoryCapability(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }
}
