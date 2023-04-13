package org.metadatacenter.server.security.model.permission.resource;

public class ResourcePermissionUserPermissionPair {

  private ResourcePermissionUser user;
  private FilesystemResourcePermission permission;

  public ResourcePermissionUserPermissionPair() {
  }

  public ResourcePermissionUserPermissionPair(ResourcePermissionUser user, FilesystemResourcePermission permission) {
    this.user = user;
    this.permission = permission;
  }

  public ResourcePermissionUser getUser() {
    return user;
  }

  public void setUser(ResourcePermissionUser user) {
    this.user = user;
  }

  public FilesystemResourcePermission getPermission() {
    return permission;
  }

  public void setPermission(FilesystemResourcePermission permission) {
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

    ResourcePermissionUserPermissionPair that = (ResourcePermissionUserPermissionPair) o;

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
