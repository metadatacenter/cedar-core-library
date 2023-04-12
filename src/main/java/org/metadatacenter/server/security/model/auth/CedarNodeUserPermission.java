package org.metadatacenter.server.security.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.server.security.model.permission.resource.FilesystemResourcePermission;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionUser;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionUserPermissionPair;
import org.metadatacenter.server.security.model.user.CedarUserExtract;

@JsonIgnoreProperties({"asUserIdPermissionPair", "key"})
public class CedarNodeUserPermission extends CedarNodePermission {

  private CedarUserExtract user;

  public CedarNodeUserPermission() {
  }

  public CedarNodeUserPermission(CedarUserExtract user, FilesystemResourcePermission permission) {
    this.user = user;
    this.permission = permission;
  }

  public CedarUserExtract getUser() {
    return user;
  }

  public void setUser(CedarUserExtract user) {
    this.user = user;
  }

  @Override
  protected String getObjectId() {
    return user.getId();
  }

  public ResourcePermissionUserPermissionPair getAsUserIdPermissionPair() {
    return new ResourcePermissionUserPermissionPair(new ResourcePermissionUser(getUser().getId()), getPermission());
  }
}
