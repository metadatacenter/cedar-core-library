package org.metadatacenter.server.security.model.permission.category;

public abstract class AbstractCategoryPermission {

  protected CategoryPermission permission;
  protected static final String KEY_SEPARATOR = "|";

  public AbstractCategoryPermission() {
  }

  protected abstract String getObjectId();

  public CategoryPermission getPermission() {
    return permission;
  }

  public void setPermission(CategoryPermission permission) {
    this.permission = permission;
  }

  public String getKey() {
    return getKey(getObjectId(), permission);
  }

  public static String getKey(String objectId, CategoryPermission permission) {
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
