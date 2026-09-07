package org.metadatacenter.server.security.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.id.CedarGroupId;
import org.metadatacenter.server.security.model.permission.resource.ResourceRole;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionGroup;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionGroupPermissionPair;
import org.metadatacenter.server.security.model.user.CedarGroupExtract;

@JsonIgnoreProperties({"asGroupIdPermissionPair", "key"})
public class CedarNodeGroupIdPermission extends CedarNodePermission {

  private CedarGroupId groupId;

  public CedarNodeGroupIdPermission() {
  }

  public CedarNodeGroupIdPermission(CedarGroupId groupId, ResourceRole role) {
    this.groupId = groupId;
    this.role = role;
  }

  public CedarGroupId getGroupId() {
    return groupId;
  }

  public void setGroupId(CedarGroupId groupId) {
    this.groupId = this.groupId;
  }

  @Override
  protected String getObjectId() {
    return groupId.getId();
  }

  public ResourcePermissionGroupPermissionPair getAsGroupIdPermissionPair() {
    return new ResourcePermissionGroupPermissionPair(new ResourcePermissionGroup(getGroupId().getId()), getRole());
  }
}
