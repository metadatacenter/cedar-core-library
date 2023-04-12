package org.metadatacenter.server.security.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.id.CedarUserId;
import org.metadatacenter.server.security.model.permission.resource.FilesystemResourcePermission;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionUser;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionUserPermissionPair;
import org.metadatacenter.server.security.model.user.CedarUserExtract;

@JsonIgnoreProperties({"asUserIdPermissionPair", "key"})
public class CedarNodeUserIdPermission extends CedarNodePermission {

  private CedarUserId userId;

  public CedarNodeUserIdPermission() {
  }

  public CedarNodeUserIdPermission(CedarUserId userId, FilesystemResourcePermission permission) {
    this.userId = userId;
    this.permission = permission;
  }

  public CedarUserId getUserId() {
    return userId;
  }

  public void setUserId(CedarUserId userId) {
    this.userId = userId;
  }

  @Override
  protected String getObjectId() {
    return userId.getId();
  }

  public ResourcePermissionUserPermissionPair getAsUserIdPermissionPair() {
    return new ResourcePermissionUserPermissionPair(new ResourcePermissionUser(getUserId().getId()), getPermission());
  }
}
