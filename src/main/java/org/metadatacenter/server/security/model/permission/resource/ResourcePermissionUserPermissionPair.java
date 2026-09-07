package org.metadatacenter.server.security.model.permission.resource;

public class ResourcePermissionUserPermissionPair {

  private ResourcePermissionUser user;
  private ResourceRole role;

  public ResourcePermissionUserPermissionPair() {
  }

  public ResourcePermissionUserPermissionPair(ResourcePermissionUser user, ResourceRole role) {
    this.user = user;
    this.role = role;
  }

  public ResourcePermissionUser getUser() {
    return user;
  }

  public void setUser(ResourcePermissionUser user) {
    this.user = user;
  }

  public ResourceRole getRole() {
    return role;
  }

  public void setRole(ResourceRole role) {
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

    ResourcePermissionUserPermissionPair that = (ResourcePermissionUserPermissionPair) o;

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
