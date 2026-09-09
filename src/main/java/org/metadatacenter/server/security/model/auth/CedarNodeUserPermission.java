package org.metadatacenter.server.security.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import org.metadatacenter.server.security.model.permission.resource.ResourceRole;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionUser;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionUserPermissionPair;
import org.metadatacenter.server.security.model.user.CedarUserExtract;

@JsonIgnoreProperties({"asUserIdPermissionPair", "key"})
@Schema(name = "ResourceUserGrant", description = "One role granted to one user on a folder or artifact.")
public class CedarNodeUserPermission extends CedarNodePermission {

  private CedarUserExtract user;

  public CedarNodeUserPermission() {
  }

  public CedarNodeUserPermission(CedarUserExtract user, ResourceRole role) {
    this.user = user;
    this.role = role;
  }

  @Schema(name = "user", requiredMode = Schema.RequiredMode.REQUIRED,
      description = "The user the role is granted to.")
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
    return new ResourcePermissionUserPermissionPair(new ResourcePermissionUser(getUser().getId()), getRole());
  }
}
