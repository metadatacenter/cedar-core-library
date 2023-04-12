package org.metadatacenter.server.security.model.auth;

import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionUser;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionsRequest;
import org.metadatacenter.server.security.model.user.CedarUserExtract;

import java.util.ArrayList;
import java.util.List;

public class CedarNodePermissionsWithId {

  private CedarUserExtract owner;
  private final List<CedarNodeUserIdPermission> userPermissions;
  private final List<CedarNodeGroupIdPermission> groupPermissions;


  public CedarNodePermissionsWithId() {
    userPermissions = new ArrayList<>();
    groupPermissions = new ArrayList<>();
  }

  public CedarUserExtract getOwner() {
    return owner;
  }

  public void setOwner(CedarUserExtract owner) {
    this.owner = owner;
  }

  public List<CedarNodeUserIdPermission> getUserPermissions() {
    return userPermissions;
  }

  public void addUserPermissions(CedarNodeUserIdPermission userPermission) {
    userPermissions.add(userPermission);
  }

  public List<CedarNodeGroupIdPermission> getGroupPermissions() {
    return groupPermissions;
  }

  public void addGroupPermissions(CedarNodeGroupIdPermission groupPermission) {
    groupPermissions.add(groupPermission);
  }

  public ResourcePermissionsRequest toRequest() {
    ResourcePermissionsRequest r = new ResourcePermissionsRequest();
    // copy owner
    ResourcePermissionUser newOwner = new ResourcePermissionUser();
    newOwner.setId(owner.getId());
    r.setOwner(newOwner);
    // copy users
    for (CedarNodeUserIdPermission up : userPermissions) {
      r.getUserPermissions().add(up.getAsUserIdPermissionPair());
    }
    // copy groups
    for (CedarNodeGroupIdPermission gp : groupPermissions) {
      r.getGroupPermissions().add(gp.getAsGroupIdPermissionPair());
    }
    return r;
  }
}
