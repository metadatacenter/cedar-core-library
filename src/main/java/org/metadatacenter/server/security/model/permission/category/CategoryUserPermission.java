package org.metadatacenter.server.security.model.permission.category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.server.security.model.user.CedarUserExtract;

@JsonIgnoreProperties({"asUserIdPermissionPair"})
public class CategoryUserPermission extends AbstractCategoryPermission {

  private CedarUserExtract user;

  public CategoryUserPermission() {
  }

  public CategoryUserPermission(CedarUserExtract user, CategoryRole role) {
    this.user = user;
    this.role = role;
  }

  public CedarUserExtract getUser() {
    return user;
  }

  public void setUser(CedarUserExtract user) {
    this.user = user;
  }

  public CategoryPermissionUserPermissionPair getAsUserIdPermissionPair() {
    return new CategoryPermissionUserPermissionPair(new CategoryPermissionUser(getUser().getId()), getRole());
  }
}
