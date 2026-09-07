package org.metadatacenter.server.security.model.permission.category;

public class CategoryPermissionUserPermissionPair {

  private CategoryPermissionUser user;
  private CategoryRole role;

  public CategoryPermissionUserPermissionPair() {
  }

  public CategoryPermissionUserPermissionPair(CategoryPermissionUser user, CategoryRole role) {
    this.user = user;
    this.role = role;
  }

  public CategoryPermissionUser getUser() {
    return user;
  }

  public void setUser(CategoryPermissionUser user) {
    this.user = user;
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

    CategoryPermissionUserPermissionPair that = (CategoryPermissionUserPermissionPair) o;

    if (getUser() != null ? !getUser().equals(that.getUser()) : that.getUser() != null) {
      return false;
    }
    return getRole() == that.getRole();

  }

  @Override
  public int hashCode() {
    int result = getUser() != null ? getUser().hashCode() : 0;
    result = 31 * result + (getRole() != null ? getRole().hashCode() : 0);
    return result;
  }
}
