package org.metadatacenter.server.security.model.permission.category;

public class CategoryPermissionGroupPermissionPair {

  private CategoryPermissionGroup group;
  private CategoryRole role;

  public CategoryPermissionGroupPermissionPair() {
  }

  public CategoryPermissionGroupPermissionPair(CategoryPermissionGroup group, CategoryRole role) {
    this.group = group;
    this.role = role;
  }

  public CategoryPermissionGroup getGroup() {
    return group;
  }

  public void setGroup(CategoryPermissionGroup group) {
    this.group = group;
  }

  public CategoryRole getRole() {
    return role;
  }

  public void setRole(CategoryRole role) {
    this.role = role;
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
    return getRole() == that.getRole();

  }

  @Override
  public int hashCode() {
    int result = getGroup() != null ? getGroup().hashCode() : 0;
    result = 31 * result + (getRole() != null ? getRole().hashCode() : 0);
    return result;
  }
}
