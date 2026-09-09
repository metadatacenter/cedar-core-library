package org.metadatacenter.server.security.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import org.metadatacenter.server.security.model.permission.resource.ResourceRole;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionGroup;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionGroupPermissionPair;
import org.metadatacenter.server.security.model.user.CedarGroupExtract;

@JsonIgnoreProperties({"asGroupIdPermissionPair", "key"})
@Schema(name = "ResourceGroupGrant", description = "One role granted to one group on a folder or artifact.")
public class CedarNodeGroupPermission extends CedarNodePermission {

  private CedarGroupExtract group;

  public CedarNodeGroupPermission() {
  }

  public CedarNodeGroupPermission(CedarGroupExtract group, ResourceRole role) {
    this.group = group;
    this.role = role;
  }

  @Schema(name = "group", requiredMode = Schema.RequiredMode.REQUIRED,
      description = "The group the role is granted to.")
  public CedarGroupExtract getGroup() {
    return group;
  }

  public void setGroup(CedarGroupExtract group) {
    this.group = group;
  }

  @Override
  protected String getObjectId() {
    return group.getId();
  }

  public ResourcePermissionGroupPermissionPair getAsGroupIdPermissionPair() {
    return new ResourcePermissionGroupPermissionPair(new ResourcePermissionGroup(getGroup().getId()), getRole());
  }
}
