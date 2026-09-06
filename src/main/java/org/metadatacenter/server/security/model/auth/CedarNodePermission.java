package org.metadatacenter.server.security.model.auth;

import org.metadatacenter.server.security.model.permission.resource.ResourceRole;

public abstract class CedarNodePermission {

  protected ResourceRole role;
  protected static final String KEY_SEPARATOR = "|";

  public CedarNodePermission() {
  }

  protected abstract String getObjectId();

  public ResourceRole getRole() {
    return role;
  }

  public void setRole(ResourceRole role) {
    this.role = role;
  }

  public String getKey() {
    return getKey(getObjectId(), role);
  }

  public static String getKey(String objectId, ResourceRole role) {
    return objectId + KEY_SEPARATOR + role.getValue();
  }

  public static String getId(String key) {
    if (key != null) {
      int pos = key.indexOf(KEY_SEPARATOR);
      if (pos != -1) {
        return key.substring(0, pos);
      }
    }
    return null;
  }
}
