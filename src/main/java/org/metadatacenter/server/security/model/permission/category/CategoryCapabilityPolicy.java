package org.metadatacenter.server.security.model.permission.category;

import org.metadatacenter.server.security.model.auth.CedarPermission;
import org.metadatacenter.server.security.model.user.CedarUser;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import static org.metadatacenter.server.security.model.permission.category.CategoryCapability.*;

/** The single context-aware calculation of capabilities on categories. */
public final class CategoryCapabilityPolicy {

  private static final Set<CategoryCapability> ROOT_RESTRICTIONS = Set.of(
      ATTACH_CATEGORY, DETACH_CATEGORY, UPDATE_CATEGORY, DELETE_CATEGORY, MOVE_CATEGORY,
      TRANSFER_OWNERSHIP);

  private CategoryCapabilityPolicy() {
  }

  public static Set<CategoryCapability> evaluate(CategoryAuthority authority,
                                                 CategoryAccessContext context,
                                                 CedarUser user) {
    if (authority == null || context == null) {
      return Collections.emptySet();
    }
    EnumSet<CategoryCapability> capabilities = copy(authority.capabilities());
    addAdministrativeCapabilities(capabilities, user);
    if (context.root()) {
      capabilities.removeAll(ROOT_RESTRICTIONS);
    }
    return Collections.unmodifiableSet(capabilities);
  }

  public static boolean provides(CategoryAuthority authority, CategoryAccessContext context,
                                 CedarUser user, CategoryCapability capability) {
    return capability != null && evaluate(authority, context, user).contains(capability);
  }

  private static void addAdministrativeCapabilities(EnumSet<CategoryCapability> capabilities,
                                                    CedarUser user) {
    if (user == null) {
      return;
    }
    if (user.has(CedarPermission.WRITE_NOT_WRITABLE_CATEGORY)) {
      capabilities.add(ATTACH_CATEGORY);
      capabilities.add(DETACH_CATEGORY);
      capabilities.add(UPDATE_CATEGORY);
      capabilities.add(CREATE_CHILD_CATEGORY);
      capabilities.add(DELETE_CATEGORY);
      capabilities.add(MOVE_CATEGORY);
    }
    if (user.has(CedarPermission.UPDATE_PERMISSION_NOT_WRITABLE_CATEGORY)) {
      capabilities.add(MANAGE_GRANTS);
    }
  }

  private static EnumSet<CategoryCapability> copy(Set<CategoryCapability> capabilities) {
    return capabilities == null || capabilities.isEmpty()
        ? EnumSet.noneOf(CategoryCapability.class)
        : EnumSet.copyOf(capabilities);
  }
}
