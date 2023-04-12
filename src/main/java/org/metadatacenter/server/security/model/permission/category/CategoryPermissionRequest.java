package org.metadatacenter.server.security.model.permission.category;

import java.util.ArrayList;
import java.util.List;

public class CategoryPermissionRequest {

  private CategoryPermissionUser owner;
  private List<CategoryPermissionUserPermissionPair> userPermissions;
  private List<CategoryPermissionGroupPermissionPair> groupPermissions;


  public CategoryPermissionRequest() {
    userPermissions = new ArrayList<>();
    groupPermissions = new ArrayList<>();
  }

  public CategoryPermissionUser getOwner() {
    return owner;
  }

  public void setOwner(CategoryPermissionUser owner) {
    this.owner = owner;
  }

  public List<CategoryPermissionUserPermissionPair> getUserPermissions() {
    return userPermissions;
  }

  public void setUserPermissions(List<CategoryPermissionUserPermissionPair> userPermissions) {
    this.userPermissions = userPermissions;
  }

  public List<CategoryPermissionGroupPermissionPair> getGroupPermissions() {
    return groupPermissions;
  }

  public void setGroupPermissions(List<CategoryPermissionGroupPermissionPair> groupPermissions) {
    this.groupPermissions = groupPermissions;
  }
}
