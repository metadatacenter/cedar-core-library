package org.metadatacenter.server.security.model.permission.category;

public class CategoryPermissionUserPermissionPair {

  private CategoryPermissionUser user;
  private CategoryPermission permission;

  public CategoryPermissionUserPermissionPair() {
  }

  public CategoryPermissionUserPermissionPair(CategoryPermissionUser user, CategoryPermission permission) {
    this.user = user;
    this.permission = permission;
  }

  public CategoryPermissionUser getUser() {
    return user;
  }

  public void setUser(CategoryPermissionUser user) {
    this.user = user;
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

    CategoryPermissionUserPermissionPair that = (CategoryPermissionUserPermissionPair) o;

    if (getUser() != null ? !getUser().equals(that.getUser()) : that.getUser() != null) {
      return false;
    }
    return getPermission() == that.getPermission();

  }

  @Override
  public int hashCode() {
    int result = getUser() != null ? getUser().hashCode() : 0;
    result = 31 * result + (getPermission() != null ? getPermission().hashCode() : 0);
    return result;
  }
}
