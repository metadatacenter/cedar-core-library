package org.metadatacenter.server.security.model.permission.resource;

public class ResourcePermissionGroupPermissionPair {

  private ResourcePermissionGroup group;
  private FilesystemResourcePermission permission;

  public ResourcePermissionGroupPermissionPair() {
  }

  public ResourcePermissionGroupPermissionPair(ResourcePermissionGroup group, FilesystemResourcePermission permission) {
    this.group = group;
    this.permission = permission;
  }

  public ResourcePermissionGroup getGroup() {
    return group;
  }

  public void setGroup(ResourcePermissionGroup group) {
    this.group = group;
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

    ResourcePermissionGroupPermissionPair that = (ResourcePermissionGroupPermissionPair) o;

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
