package org.metadatacenter.server.security.model.permission.resource;

public class ResourcePermissionGroupPermissionPair {

  private ResourcePermissionGroup group;
  private ResourceRole role;

  public ResourcePermissionGroupPermissionPair() {
  }

  public ResourcePermissionGroupPermissionPair(ResourcePermissionGroup group, ResourceRole role) {
    this.group = group;
    this.role = role;
  }

  public ResourcePermissionGroup getGroup() {
    return group;
  }

  public void setGroup(ResourcePermissionGroup group) {
    this.group = group;
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

    ResourcePermissionGroupPermissionPair that = (ResourcePermissionGroupPermissionPair) o;

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
