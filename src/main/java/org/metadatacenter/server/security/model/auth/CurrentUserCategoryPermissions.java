package org.metadatacenter.server.security.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.error.CedarErrorKey;
import org.metadatacenter.server.security.model.permission.category.CategoryCapability;
import org.metadatacenter.server.security.model.permission.category.CategoryAuthority;
import org.metadatacenter.server.security.model.permission.category.CategoryRole;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

@JsonIgnoreProperties(value = {
    "currentUserRole", "canRead", "canWrite", "canDelete", "canShare", "canChangeOwner",
    "canAttach", "canDetach", "canEdit", "canCreateChild", "canManageGrants", "canMove",
    "canTransferOwnership"
})
public class CurrentUserCategoryPermissions {
  private CategoryRole role;
  private boolean owner;
  private Set<CategoryCapability> capabilities = Collections.emptySet();

  private CedarErrorKey createDraftErrorKey;
  private CedarErrorKey publishErrorKey;

  public CategoryRole getRole() {
    return role;
  }

  public void setRole(CategoryRole role) {
    this.role = role;
  }

  public boolean isOwner() {
    return owner;
  }

  public void setOwner(boolean owner) {
    this.owner = owner;
  }

  public Set<CategoryCapability> getCapabilities() {
    return capabilities;
  }

  public void setCapabilities(Set<CategoryCapability> capabilities) {
    this.capabilities = capabilities == null || capabilities.isEmpty()
        ? Collections.emptySet()
        : Collections.unmodifiableSet(EnumSet.copyOf(capabilities));
  }

  public void applyAuthority(CategoryAuthority authority) {
    applyAccess(authority, authority.capabilities());
  }

  public void applyAccess(CategoryAuthority authority, Set<CategoryCapability> effectiveCapabilities) {
    setOwner(authority.owner());
    setRole(authority.role());
    setCapabilities(effectiveCapabilities);
  }

  public CategoryRole getCurrentUserRole() {
    return role;
  }

  public void setCurrentUserRole(CategoryRole currentUserRole) {
    setRole(currentUserRole);
  }

  public boolean isCanRead() {
    return capabilities.contains(CategoryCapability.READ_CATEGORY);
  }

  public boolean isCanWrite() {
    return capabilities.contains(CategoryCapability.UPDATE_CATEGORY)
        && capabilities.contains(CategoryCapability.MANAGE_GRANTS);
  }

  public boolean isCanDelete() {
    return capabilities.contains(CategoryCapability.DELETE_CATEGORY);
  }

  public boolean isCanShare() {
    return capabilities.contains(CategoryCapability.MANAGE_GRANTS);
  }

  public boolean isCanChangeOwner() {
    return capabilities.contains(CategoryCapability.TRANSFER_OWNERSHIP);
  }

  public boolean isCanAttach() {
    return capabilities.contains(CategoryCapability.ATTACH_CATEGORY);
  }

  public boolean isCanDetach() {
    return capabilities.contains(CategoryCapability.DETACH_CATEGORY);
  }

  public boolean isCanEdit() {
    return capabilities.contains(CategoryCapability.UPDATE_CATEGORY);
  }

  public boolean isCanCreateChild() {
    return capabilities.contains(CategoryCapability.CREATE_CHILD_CATEGORY);
  }

  public boolean isCanManageGrants() {
    return capabilities.contains(CategoryCapability.MANAGE_GRANTS);
  }

  public boolean isCanMove() {
    return capabilities.contains(CategoryCapability.MOVE_CATEGORY);
  }

  public boolean isCanTransferOwnership() {
    return capabilities.contains(CategoryCapability.TRANSFER_OWNERSHIP);
  }

  public CedarErrorKey getCreateDraftErrorKey() {
    return createDraftErrorKey;
  }

  public void setCreateDraftErrorKey(CedarErrorKey createDraftErrorKey) {
    this.createDraftErrorKey = createDraftErrorKey;
  }

  public CedarErrorKey getPublishErrorKey() {
    return publishErrorKey;
  }

  public void setPublishErrorKey(CedarErrorKey publishErrorKey) {
    this.publishErrorKey = publishErrorKey;
  }
}
