package org.metadatacenter.server.security.model.permission.category;

import org.metadatacenter.server.security.model.user.CedarUserExtract;

import java.util.ArrayList;
import java.util.List;

public class CategoryPermissions {

  private CedarUserExtract owner;
  private final List<CategoryUserPermission> userPermissions;
  private final List<CategoryGroupPermission> groupPermissions;


  public CategoryPermissions() {
    userPermissions = new ArrayList<>();
    groupPermissions = new ArrayList<>();
  }

  public CedarUserExtract getOwner() {
    return owner;
  }

  public void setOwner(CedarUserExtract owner) {
    this.owner = owner;
  }

  public List<CategoryUserPermission> getUserPermissions() {
    return userPermissions;
  }

  public void addUserPermissions(CategoryUserPermission userPermission) {
    userPermissions.add(userPermission);
  }

  public List<CategoryGroupPermission> getGroupPermissions() {
    return groupPermissions;
  }

  public void addGroupPermissions(CategoryGroupPermission groupPermission) {
    groupPermissions.add(groupPermission);
  }

  public CategoryPermissionRequest toRequest() {
    CategoryPermissionRequest r = new CategoryPermissionRequest();
    // copy owner
    CategoryPermissionUser newOwner = new CategoryPermissionUser();
    newOwner.setId(owner.getId());
    r.setOwner(newOwner);
    // copy users
    for (CategoryUserPermission up : userPermissions) {
      r.getUserPermissions().add(up.getAsUserIdPermissionPair());
    }
    // copy groups
    for (CategoryGroupPermission gp : groupPermissions) {
      r.getGroupPermissions().add(gp.getAsGroupIdPermissionPair());
    }
    return r;
  }
}
