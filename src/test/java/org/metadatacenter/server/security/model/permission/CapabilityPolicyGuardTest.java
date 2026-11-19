package org.metadatacenter.server.security.model.permission;

import org.junit.jupiter.api.Test;
import org.metadatacenter.model.CedarResourceType;
import org.metadatacenter.server.security.model.auth.CedarPermission;
import org.metadatacenter.server.security.model.permission.category.CategoryAccessContext;
import org.metadatacenter.server.security.model.permission.category.CategoryAuthority;
import org.metadatacenter.server.security.model.permission.category.CategoryCapability;
import org.metadatacenter.server.security.model.permission.category.CategoryCapabilityPolicy;
import org.metadatacenter.server.security.model.permission.category.CategoryRole;
import org.metadatacenter.server.security.model.permission.resource.ResourceAccessContext;
import org.metadatacenter.server.security.model.permission.resource.ResourceAuthority;
import org.metadatacenter.server.security.model.permission.resource.ResourceCapability;
import org.metadatacenter.server.security.model.permission.resource.ResourceCapabilityPolicy;
import org.metadatacenter.server.security.model.permission.resource.ResourceRole;
import org.metadatacenter.server.security.model.user.CedarUser;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The capability policies fail closed: a missing authority, context or resource type grants nothing,
 * a missing capability is never provided, and a missing user adds no administrative capability.
 */
class CapabilityPolicyGuardTest {

  private static final ResourceAuthority MANAGER = new ResourceAuthority(ResourceRole.MANAGER, false);
  private static final ResourceAccessContext TEMPLATE = ResourceAccessContext.ordinary(CedarResourceType.TEMPLATE);
  private static final CategoryAuthority CATEGORY_MANAGER = new CategoryAuthority(CategoryRole.MANAGER, false);
  private static final CategoryAccessContext CATEGORY = new CategoryAccessContext(false);

  private static CedarUser userWithPermissions(CedarPermission... permissions) {
    CedarUser user = new CedarUser();
    user.setPermissions(Stream.of(permissions).map(CedarPermission::getPermissionName).toList());
    return user;
  }

  private static final CedarUser RESOURCE_ADMINISTRATOR = userWithPermissions(
      CedarPermission.READ_NOT_READABLE_NODE, CedarPermission.WRITE_NOT_WRITABLE_NODE,
      CedarPermission.UPDATE_PERMISSION_NOT_WRITABLE_NODE);
  private static final CedarUser CATEGORY_ADMINISTRATOR = userWithPermissions(
      CedarPermission.WRITE_NOT_WRITABLE_CATEGORY, CedarPermission.UPDATE_PERMISSION_NOT_WRITABLE_CATEGORY);

  @Test
  void aResourceRequestMissingItsAuthorityContextOrTypeGrantsNothing() {
    assertEquals(Set.of(), ResourceCapabilityPolicy.evaluate(null, TEMPLATE, RESOURCE_ADMINISTRATOR));
    assertEquals(Set.of(), ResourceCapabilityPolicy.evaluate(MANAGER, null, RESOURCE_ADMINISTRATOR));
    assertEquals(Set.of(), ResourceCapabilityPolicy.evaluate(MANAGER, new ResourceAccessContext(null, false),
        RESOURCE_ADMINISTRATOR));
  }

  @Test
  void aResourceCapabilityIsProvidedOnlyWhenNamedAndEvaluated() {
    assertTrue(ResourceCapabilityPolicy.provides(MANAGER, TEMPLATE, null, ResourceCapability.MANAGE_GRANTS));
    assertFalse(ResourceCapabilityPolicy.provides(MANAGER, TEMPLATE, RESOURCE_ADMINISTRATOR, null));
    assertFalse(ResourceCapabilityPolicy.provides(null, TEMPLATE, RESOURCE_ADMINISTRATOR,
        ResourceCapability.READ_RESOURCE));
  }

  @Test
  void aMissingUserAddsNoAdministrativeResourceCapability() {
    ResourceAuthority noGrant = new ResourceAuthority(null, false);
    assertEquals(Set.of(), ResourceCapabilityPolicy.evaluate(noGrant, TEMPLATE, null));
    assertEquals(ResourceCapabilityPolicy.evaluate(MANAGER, TEMPLATE, userWithPermissions()),
        ResourceCapabilityPolicy.evaluate(MANAGER, TEMPLATE, null));
  }

  @Test
  void aCategoryRequestMissingItsAuthorityOrContextGrantsNothing() {
    assertEquals(Set.of(), CategoryCapabilityPolicy.evaluate(null, CATEGORY, CATEGORY_ADMINISTRATOR));
    assertEquals(Set.of(), CategoryCapabilityPolicy.evaluate(CATEGORY_MANAGER, null, CATEGORY_ADMINISTRATOR));
  }

  @Test
  void aCategoryCapabilityIsProvidedOnlyWhenNamedAndEvaluated() {
    assertTrue(CategoryCapabilityPolicy.provides(CATEGORY_MANAGER, CATEGORY, null, CategoryCapability.MANAGE_GRANTS));
    assertFalse(CategoryCapabilityPolicy.provides(CATEGORY_MANAGER, CATEGORY, CATEGORY_ADMINISTRATOR, null));
    assertFalse(CategoryCapabilityPolicy.provides(null, CATEGORY, CATEGORY_ADMINISTRATOR,
        CategoryCapability.UPDATE_CATEGORY));
  }

  @Test
  void aMissingUserAddsNoAdministrativeCategoryCapability() {
    CategoryAuthority noGrant = new CategoryAuthority(null, false);
    assertEquals(Set.of(), CategoryCapabilityPolicy.evaluate(noGrant, CATEGORY, null));
    assertEquals(CategoryCapabilityPolicy.evaluate(CATEGORY_MANAGER, CATEGORY, userWithPermissions()),
        CategoryCapabilityPolicy.evaluate(CATEGORY_MANAGER, CATEGORY, null));
  }
}
