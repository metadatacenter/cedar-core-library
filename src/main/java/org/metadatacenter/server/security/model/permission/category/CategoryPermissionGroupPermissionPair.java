package org.metadatacenter.server.security.model.permission.category;

public class CategoryPermissionGroupPermissionPair {

  private CategoryPermissionGroup group;
  private CategoryPermission permission;

  public CategoryPermissionGroupPermissionPair() {
  }

  public CategoryPermissionGroupPermissionPair(CategoryPermissionGroup group, CategoryPermission permission) {
    this.group = group;
    this.permission = permission;
  }

  public CategoryPermissionGroup getGroup() {
    return group;
  }

  public void setGroup(CategoryPermissionGroup group) {
    this.group = group;
  }

  public CategoryPermission getPermission() {
    return permission;
  }

  public void setPermission(CategoryPermission permission) {
    this.permission = permission;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CategoryPermissionGroupPermissionPair that = (CategoryPermissionGroupPermissionPair) o;

    if (getGroup() != null ? !getGroup().equals(that.getGroup()) : that.getGroup() != null) {
      return false;
    }
    return getPermission() == that.getPermission();

  }

  @Override
  public int hashCode() {
    int result = getGroup() != null ? getGroup().hashCode() : 0;
    result = 31 * result + (getPermission() != null ? getPermission().hashCode() : 0);
    return result;
  }
}
