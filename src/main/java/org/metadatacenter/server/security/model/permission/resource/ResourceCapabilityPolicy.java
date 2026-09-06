package org.metadatacenter.server.security.model.permission.resource;

import org.metadatacenter.model.CedarResourceType;
import org.metadatacenter.server.security.model.auth.CedarPermission;
import org.metadatacenter.server.security.model.user.CedarUser;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import static org.metadatacenter.server.security.model.permission.resource.ResourceCapability.*;

/** The single type- and context-aware calculation of capabilities on artifacts and folders. */
public final class ResourceCapabilityPolicy {

  private static final Set<ResourceCapability> PROTECTED_FOLDER_RESTRICTIONS = Set.of(
      DELETE_RESOURCE, MANAGE_GRANTS, MOVE_RESOURCE, MANAGE_OPENVIEW, TRANSFER_OWNERSHIP);

  private ResourceCapabilityPolicy() {
  }

  public static Set<ResourceCapability> evaluate(ResourceAuthority authority,
                                                  ResourceAccessContext context,
                                                  CedarUser user) {
    if (authority == null || context == null || context.resourceType() == null) {
      return Collections.emptySet();
    }
    CedarResourceType resourceType = context.resourceType();
    EnumSet<ResourceCapability> capabilities = copy(authority.capabilitiesFor(resourceType));
    addAdministrativeCapabilities(capabilities, resourceType, user);
    capabilities.removeIf(capability -> !capability.appliesTo(resourceType));
    if (context.protectedFolder()) {
      capabilities.removeAll(PROTECTED_FOLDER_RESTRICTIONS);
    }
    return Collections.unmodifiableSet(capabilities);
  }

  public static boolean provides(ResourceAuthority authority, ResourceAccessContext context,
                                 CedarUser user, ResourceCapability capability) {
    return capability != null && evaluate(authority, context, user).contains(capability);
  }

  private static void addAdministrativeCapabilities(EnumSet<ResourceCapability> capabilities,
                                                     CedarResourceType resourceType,
                                                     CedarUser user) {
    if (user == null) {
      return;
    }
    if (user.has(CedarPermission.READ_NOT_READABLE_NODE)) {
      capabilities.add(READ_RESOURCE);
      if (resourceType == CedarResourceType.FOLDER) {
        capabilities.add(LIST_FOLDER_CONTENTS);
      }
    }
    if (user.has(CedarPermission.WRITE_NOT_WRITABLE_NODE)) {
      capabilities.add(UPDATE_RESOURCE);
      capabilities.add(DELETE_RESOURCE);
      capabilities.add(MOVE_RESOURCE);
      capabilities.add(MANAGE_OPENVIEW);
      if (resourceType == CedarResourceType.FOLDER) {
        capabilities.add(CREATE_IN_FOLDER);
        capabilities.add(COPY_INTO_FOLDER);
        capabilities.add(MOVE_INTO_FOLDER);
      }
    }
    if (user.has(CedarPermission.UPDATE_PERMISSION_NOT_WRITABLE_NODE)) {
      capabilities.add(MANAGE_GRANTS);
    }
  }

  private static EnumSet<ResourceCapability> copy(Set<ResourceCapability> capabilities) {
    return capabilities == null || capabilities.isEmpty()
        ? EnumSet.noneOf(ResourceCapability.class)
        : EnumSet.copyOf(capabilities);
  }
}
