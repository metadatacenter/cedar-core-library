package org.metadatacenter.server.security.model.permission.category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.server.security.model.user.CedarGroupExtract;

@JsonIgnoreProperties({"asGroupIdPermissionPair", "key"})
public class CategoryGroupPermission extends AbstractCategoryPermission {

  private CedarGroupExtract group;

  public CategoryGroupPermission() {
  }

  public CategoryGroupPermission(CedarGroupExtract group, CategoryPermission permission) {
    this.group = group;
    this.permission = permission;
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

  public CategoryPermissionGroupPermissionPair getAsGroupIdPermissionPair() {
    return new CategoryPermissionGroupPermissionPair(new CategoryPermissionGroup(getGroup().getId()), getPermission());
  }
}
