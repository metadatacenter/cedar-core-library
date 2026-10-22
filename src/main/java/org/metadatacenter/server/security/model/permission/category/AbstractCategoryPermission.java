package org.metadatacenter.server.security.model.permission.category;

public abstract class AbstractCategoryPermission {

  protected CategoryRole role;

  public AbstractCategoryPermission() {
  }

  public CategoryRole getRole() {
    return role;
  }

  public void setRole(CategoryRole role) {
    this.role = role;
  }
}
