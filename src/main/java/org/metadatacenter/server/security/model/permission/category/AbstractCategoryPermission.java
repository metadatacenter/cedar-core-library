package org.metadatacenter.server.security.model.permission.category;

public abstract class AbstractCategoryPermission {

  protected CategoryRole role;
  protected static final String KEY_SEPARATOR = "|";

  public AbstractCategoryPermission() {
  }

  protected abstract String getObjectId();

  public CategoryRole getRole() {
    return role;
  }

  public void setRole(CategoryRole role) {
    this.role = role;
  }

  public String getKey() {
    return getKey(getObjectId(), role);
  }

  public static String getKey(String objectId, CategoryRole role) {
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
