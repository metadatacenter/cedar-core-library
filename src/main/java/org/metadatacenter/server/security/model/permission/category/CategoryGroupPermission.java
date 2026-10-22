package org.metadatacenter.server.security.model.permission.category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.server.security.model.user.CedarGroupExtract;

@JsonIgnoreProperties({"asGroupIdPermissionPair"})
public class CategoryGroupPermission extends AbstractCategoryPermission {

  private CedarGroupExtract group;

  public CategoryGroupPermission() {
  }

  public CategoryGroupPermission(CedarGroupExtract group, CategoryRole role) {
    this.group = group;
    this.role = role;
  }

  public CedarGroupExtract getGroup() {
    return group;
  }

  public void setGroup(CedarGroupExtract group) {
    this.group = group;
  }

  public CategoryPermissionGroupPermissionPair getAsGroupIdPermissionPair() {
    return new CategoryPermissionGroupPermissionPair(new CategoryPermissionGroup(getGroup().getId()), getRole());
  }
}
