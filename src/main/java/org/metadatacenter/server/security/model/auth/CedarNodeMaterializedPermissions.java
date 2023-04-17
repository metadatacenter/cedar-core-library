package org.metadatacenter.server.security.model.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.id.CedarFilesystemResourceId;
import org.metadatacenter.server.security.model.permission.resource.FilesystemResourcePermission;

import java.util.HashMap;
import java.util.Map;

public class CedarNodeMaterializedPermissions {

  private final String id;
  private final Map<String, FilesystemResourcePermission> userPermissions;
  private final Map<String, FilesystemResourcePermission> groupPermissions;
  private final NodeSharePermission everybodyPermission;


  public CedarNodeMaterializedPermissions(CedarFilesystemResourceId resourceId,
                                          NodeSharePermission everybodyPermission) {
    this.id = resourceId.getId();
    this.everybodyPermission = everybodyPermission;
    userPermissions = new HashMap<>();
    groupPermissions = new HashMap<>();
  }

  @JsonProperty("@id")
  public String getId() {
    return id;
  }

  public NodeSharePermission getEverybodyPermission() {
    return everybodyPermission;
  }

  public Map<String, FilesystemResourcePermission> getUserPermissions() {
    return userPermissions;
  }

  public void setUserPermission(String id, FilesystemResourcePermission userPermission) {
    userPermissions.put(id, userPermission);
  }

  public Map<String, FilesystemResourcePermission> getGroupPermissions() {
    return groupPermissions;
  }

  public void setGroupPermission(String id, FilesystemResourcePermission groupPermission) {
    groupPermissions.put(id, groupPermission);
  }

  public static String getKey(String userOrGroupId, FilesystemResourcePermission permission) {
    StringBuilder sb = new StringBuilder();
    sb.append(userOrGroupId);
    sb.append("|");
    sb.append(permission.getValue());
    return sb.toString();
  }
}
