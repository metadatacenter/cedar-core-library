package org.metadatacenter.server.security.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.error.CedarErrorKey;
import org.metadatacenter.model.CedarResourceType;
import org.metadatacenter.server.security.model.permission.resource.ResourceAction;
import org.metadatacenter.server.security.model.permission.resource.ResourceAuthority;
import org.metadatacenter.server.security.model.permission.resource.ResourceCapability;
import org.metadatacenter.server.security.model.permission.resource.ResourceRole;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true, value = {
    "currentUserRole", "canRead", "canView", "canEdit", "canCreate", "canWrite", "canDelete",
    "canShare", "canManageGrants", "canMove", "canManageOpenView", "canTransferOwnership",
    "canChangeOwner", "canCreateDraft", "canPublish", "canSubmit", "canPopulate", "canCopy",
    "canMakeOpen", "canMakeNotOpen"
})
public class CurrentUserResourcePermissions {
  private ResourceRole role;
  private Set<ResourceCapability> capabilities = Collections.emptySet();
  private Set<ResourceAction> availableActions = Collections.emptySet();

  private boolean owner;

  private CedarErrorKey createDraftErrorKey;
  private CedarErrorKey publishErrorKey;

  public ResourceRole getRole() {
    return role;
  }

  public void setRole(ResourceRole role) {
    this.role = role;
  }

  public Set<ResourceCapability> getCapabilities() {
    return capabilities;
  }

  public void setCapabilities(Set<ResourceCapability> capabilities) {
    this.capabilities = immutableEnumSet(capabilities, ResourceCapability.class);
  }

  public void removeCapabilities(ResourceCapability... capabilitiesToRemove) {
    EnumSet<ResourceCapability> retained = copyCapabilities();
    for (ResourceCapability capability : capabilitiesToRemove) {
      retained.remove(capability);
    }
    setCapabilities(retained);
  }

  public Set<ResourceAction> getAvailableActions() {
    return availableActions;
  }

  public void setAvailableActions(Set<ResourceAction> availableActions) {
    this.availableActions = immutableEnumSet(availableActions, ResourceAction.class);
  }

  public void setActionAvailable(ResourceAction action, boolean available) {
    EnumSet<ResourceAction> actions = copyActions();
    if (available) {
      actions.add(action);
    } else {
      actions.remove(action);
    }
    setAvailableActions(actions);
  }

  public void applyAuthority(ResourceAuthority authority, CedarResourceType resourceType) {
    applyAccess(authority, authority.capabilitiesFor(resourceType));
  }

  public void applyAccess(ResourceAuthority authority, Set<ResourceCapability> capabilities) {
    setOwner(authority.owner());
    setRole(authority.role());
    setCapabilities(capabilities);
  }

  public ResourceRole getCurrentUserRole() {
    return role;
  }

  public void setCurrentUserRole(ResourceRole currentUserRole) {
    setRole(currentUserRole);
  }

  public boolean isOwner() {
    return owner;
  }

  public void setOwner(boolean owner) {
    this.owner = owner;
  }

  public boolean isCanRead() {
    return capabilities.contains(ResourceCapability.READ_RESOURCE);
  }

  public boolean isCanView() {
    return isCanRead();
  }

  public boolean isCanEdit() {
    return capabilities.contains(ResourceCapability.UPDATE_RESOURCE);
  }

  public boolean isCanCreate() {
    return capabilities.contains(ResourceCapability.CREATE_IN_FOLDER);
  }

  public boolean isCanWrite() {
    return owner || role == ResourceRole.MANAGER
        || capabilities.contains(ResourceCapability.UPDATE_RESOURCE)
        && capabilities.contains(ResourceCapability.MANAGE_GRANTS);
  }

  public boolean isCanDelete() {
    return capabilities.contains(ResourceCapability.DELETE_RESOURCE);
  }

  public boolean isCanShare() {
    return capabilities.contains(ResourceCapability.MANAGE_GRANTS);
  }

  public boolean isCanManageGrants() {
    return capabilities.contains(ResourceCapability.MANAGE_GRANTS);
  }

  public boolean isCanMove() {
    return capabilities.contains(ResourceCapability.MOVE_RESOURCE);
  }

  public boolean isCanManageOpenView() {
    return capabilities.contains(ResourceCapability.MANAGE_OPENVIEW);
  }

  public boolean isCanTransferOwnership() {
    return capabilities.contains(ResourceCapability.TRANSFER_OWNERSHIP);
  }

  public boolean isCanChangeOwner() {
    return isCanTransferOwnership();
  }

  public boolean isCanCreateDraft() {
    return availableActions.contains(ResourceAction.CREATE_DRAFT);
  }

  public boolean isCanPublish() {
    return availableActions.contains(ResourceAction.PUBLISH);
  }

  public boolean isCanSubmit() {
    return availableActions.contains(ResourceAction.SUBMIT);
  }

  public boolean isCanPopulate() {
    return availableActions.contains(ResourceAction.POPULATE);
  }

  public boolean isCanCopy() {
    return availableActions.contains(ResourceAction.COPY_FROM_RESOURCE);
  }

  public boolean isCanMakeOpen() {
    return availableActions.contains(ResourceAction.ENABLE_OPENVIEW);
  }

  public boolean isCanMakeNotOpen() {
    return availableActions.contains(ResourceAction.DISABLE_OPENVIEW);
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

  private EnumSet<ResourceCapability> copyCapabilities() {
    return capabilities.isEmpty() ? EnumSet.noneOf(ResourceCapability.class) : EnumSet.copyOf(capabilities);
  }

  private EnumSet<ResourceAction> copyActions() {
    return availableActions.isEmpty() ? EnumSet.noneOf(ResourceAction.class) : EnumSet.copyOf(availableActions);
  }

  private static <E extends Enum<E>> Set<E> immutableEnumSet(Set<E> values, Class<E> enumType) {
    EnumSet<E> copy = values == null || values.isEmpty()
        ? EnumSet.noneOf(enumType)
        : EnumSet.copyOf(values);
    return Collections.unmodifiableSet(copy);
  }
}
