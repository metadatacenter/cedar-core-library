package org.metadatacenter.server.security.model.permission.category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.server.security.model.user.CedarUserExtract;

@JsonIgnoreProperties({"asUserIdPermissionPair", "key"})
public class CategoryUserPermission extends AbstractCategoryPermission {

  private CedarUserExtract user;

  public CategoryUserPermission() {
  }

  public CategoryUserPermission(CedarUserExtract user, CategoryPermission permission) {
    this.user = user;
    this.permission = permission;
  }

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

  public CategoryPermissionUserPermissionPair getAsUserIdPermissionPair() {
    return new CategoryPermissionUserPermissionPair(new CategoryPermissionUser(getUser().getId()), getPermission());
  }
}
