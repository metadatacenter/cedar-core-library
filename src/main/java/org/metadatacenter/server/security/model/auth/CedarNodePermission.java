package org.metadatacenter.server.security.model.auth;

import org.metadatacenter.server.security.model.permission.resource.FilesystemResourcePermission;

public abstract class CedarNodePermission {

  protected FilesystemResourcePermission permission;
  protected static final String KEY_SEPARATOR = "|";

  public CedarNodePermission() {
  }

  protected abstract String getObjectId();

  public FilesystemResourcePermission getPermission() {
    return permission;
  }

  public void setPermission(FilesystemResourcePermission permission) {
    this.permission = permission;
  }

  public String getKey() {
    return getKey(getObjectId(), permission);
  }

  public static String getKey(String objectId, FilesystemResourcePermission permission) {
    return objectId + KEY_SEPARATOR + permission.getValue();
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
