package org.metadatacenter.server.security.model.permission.resource;

import java.util.ArrayList;
import java.util.List;

public class ResourcePermissionsRequest {

  private ResourcePermissionUser owner;
  private List<ResourcePermissionUserPermissionPair> userPermissions;
  private List<ResourcePermissionGroupPermissionPair> groupPermissions;


  public ResourcePermissionsRequest() {
    userPermissions = new ArrayList<>();
    groupPermissions = new ArrayList<>();
  }

  public ResourcePermissionUser getOwner() {
    return owner;
  }

  public void setOwner(ResourcePermissionUser owner) {
    this.owner = owner;
  }

  public List<ResourcePermissionUserPermissionPair> getUserPermissions() {
    return userPermissions;
  }

  public void setUserPermissions(List<ResourcePermissionUserPermissionPair> userPermissions) {
    this.userPermissions = userPermissions;
  }

  public List<ResourcePermissionGroupPermissionPair> getGroupPermissions() {
    return groupPermissions;
  }

  public void setGroupPermissions(List<ResourcePermissionGroupPermissionPair> groupPermissions) {
    this.groupPermissions = groupPermissions;
  }
}
