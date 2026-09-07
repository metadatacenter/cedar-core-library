package org.metadatacenter.server.security.model.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.id.CedarFilesystemResourceId;
import org.metadatacenter.server.security.model.permission.resource.ResourceRole;

import java.util.HashMap;
import java.util.Map;

public class CedarNodeMaterializedPermissions {

  private final String id;
  private final Map<String, ResourceRole> userRoles;
  private final Map<String, ResourceRole> groupRoles;
  private final NodeSharePermission everybodyPermission;


  public CedarNodeMaterializedPermissions(CedarFilesystemResourceId resourceId,
                                          NodeSharePermission everybodyPermission) {
    this.id = resourceId.getId();
    this.everybodyPermission = everybodyPermission;
    userRoles = new HashMap<>();
    groupRoles = new HashMap<>();
  }

  @JsonProperty("@id")
  public String getId() {
    return id;
  }

  public NodeSharePermission getEverybodyPermission() {
    return everybodyPermission;
  }

  public Map<String, ResourceRole> getUserRoles() {
    return userRoles;
  }

  public void setUserRole(String id, ResourceRole userRole) {
    userRoles.put(id, userRole);
  }

  public Map<String, ResourceRole> getGroupRoles() {
    return groupRoles;
  }

  public void setGroupRole(String id, ResourceRole groupRole) {
    groupRoles.put(id, groupRole);
  }

  public static String getKey(String userOrGroupId, ResourceRole role) {
    StringBuilder sb = new StringBuilder();
    sb.append(userOrGroupId);
    sb.append("|");
    sb.append(role.getValue());
    return sb.toString();
  }
}
