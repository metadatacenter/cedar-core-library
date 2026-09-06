package org.metadatacenter.server.security.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.server.security.model.permission.resource.ResourceRole;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionGroup;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionGroupPermissionPair;
import org.metadatacenter.server.security.model.user.CedarGroupExtract;

@JsonIgnoreProperties({"asGroupIdPermissionPair", "key"})
public class CedarNodeGroupPermission extends CedarNodePermission {

  private CedarGroupExtract group;

  public CedarNodeGroupPermission() {
  }

  public CedarNodeGroupPermission(CedarGroupExtract group, ResourceRole role) {
    this.group = group;
    this.role = role;
  }

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
