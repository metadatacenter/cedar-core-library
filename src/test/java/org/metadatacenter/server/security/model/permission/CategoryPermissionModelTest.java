package org.metadatacenter.server.security.model.permission;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.metadatacenter.server.security.model.auth.CedarPermission;
import org.metadatacenter.server.security.model.auth.CurrentUserCategoryPermissions;
import org.metadatacenter.server.security.model.permission.category.*;
import org.metadatacenter.server.security.model.user.CedarGroupExtract;
import org.metadatacenter.server.security.model.user.CedarUser;
import org.metadatacenter.server.security.model.user.CedarUserExtract;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/** Pure model tests for category roles, capabilities, and ACL requests. */
class CategoryPermissionModelTest {

  private static final String OWNER_ID = "https://repo.example/users/owner";
  private static final String USER_ID = "https://repo.example/users/user";
  private static final String GROUP_ID = "https://repo.example/groups/group";
  private final ObjectMapper mapper = new ObjectMapper();

  @ParameterizedTest
  @EnumSource(CategoryRole.class)
  void everyRoleValueRoundTripsThroughTheLookup(CategoryRole role) {
    assertSame(role, CategoryRole.forValue(role.getValue()));
  }

  @Test
  void unknownRoleValuesReturnNull() {
    assertNull(CategoryRole.forValue("attach"));
    assertNull(CategoryRole.forValue("write"));
    assertNull(CategoryRole.forValue("admin"));
    assertNull(CategoryRole.forValue(null));
  }

  @ParameterizedTest
  @EnumSource(CategoryRole.class)
  void resolvedRolesConvertToIdOnlyRequestPairs(CategoryRole role) {
    CategoryUserPermission userGrant = new CategoryUserPermission(
        new CedarUserExtract(USER_ID, "Given", "Family", "user@example.org"), role);
    CategoryGroupPermission groupGrant = new CategoryGroupPermission(
        new CedarGroupExtract(GROUP_ID, "Group name"), role);

    assertEquals(USER_ID, userGrant.getAsUserIdPermissionPair().getUser().getId());
    assertSame(role, userGrant.getAsUserIdPermissionPair().getRole());
    assertEquals(GROUP_ID, groupGrant.getAsGroupIdPermissionPair().getGroup().getId());
    assertSame(role, groupGrant.getAsGroupIdPermissionPair().getRole());
  }

  @Test
  void resolvedAclConvertsOwnerUsersAndGroupsToARequest() {
    CategoryPermissionRequest request = resolvedAcl().toRequest();

    assertEquals(OWNER_ID, request.getOwner().getId());
    assertEquals(new CategoryPermissionUserPermissionPair(
        new CategoryPermissionUser(USER_ID), CategoryRole.MANAGER), request.getUserPermissions().get(0));
    assertEquals(new CategoryPermissionGroupPermissionPair(
        new CategoryPermissionGroup(GROUP_ID), CategoryRole.CLASSIFIER), request.getGroupPermissions().get(0));
  }

  @ParameterizedTest
  @EnumSource(CategoryRole.class)
  void requestJsonRoundTripPreservesEveryRole(CategoryRole role) throws Exception {
    CategoryPermissionRequest request = new CategoryPermissionRequest();
    request.setOwner(new CategoryPermissionUser(OWNER_ID));
    request.getUserPermissions().add(new CategoryPermissionUserPermissionPair(
        new CategoryPermissionUser(USER_ID), role));
    request.getGroupPermissions().add(new CategoryPermissionGroupPermissionPair(
        new CategoryPermissionGroup(GROUP_ID), role));

    CategoryPermissionRequest copy = mapper.readValue(
        mapper.writeValueAsString(request), CategoryPermissionRequest.class);

    assertSame(role, copy.getUserPermissions().get(0).getRole());
    assertSame(role, copy.getGroupPermissions().get(0).getRole());
  }

  @Test
  void legacyPermissionPropertyIsRejected() {
    String json = "{\"owner\":{\"@id\":\"" + OWNER_ID + "\"},"
        + "\"userPermissions\":[{\"user\":{\"@id\":\"" + USER_ID
        + "\"},\"permission\":\"write\"}],"
        + "\"groupPermissions\":[{\"group\":{\"@id\":\"" + GROUP_ID
        + "\"},\"permission\":\"attach\"}]}";

    assertThrows(UnrecognizedPropertyException.class,
        () -> mapper.readValue(json, CategoryPermissionRequest.class));
  }

  @Test
  void rolesProvideExactlyTheCategoryCapabilities() {
    assertEquals(Set.of(CategoryCapability.READ_CATEGORY),
        new CategoryAuthority(CategoryRole.VIEWER, false).capabilities());
    assertEquals(Set.of(CategoryCapability.READ_CATEGORY, CategoryCapability.ATTACH_CATEGORY,
            CategoryCapability.DETACH_CATEGORY),
        new CategoryAuthority(CategoryRole.CLASSIFIER, false).capabilities());
    assertFalse(CategoryRole.EDITOR.provides(CategoryCapability.MANAGE_GRANTS));
    assertTrue(CategoryRole.MANAGER.provides(CategoryCapability.MOVE_CATEGORY));
  }

  @Test
  void ownershipAddsTransferWithoutBecomingAnotherRole() {
    CategoryAuthority manager = new CategoryAuthority(CategoryRole.MANAGER, false);
    CategoryAuthority owner = new CategoryAuthority(null, true);

    assertFalse(manager.provides(CategoryCapability.TRANSFER_OWNERSHIP));
    assertTrue(owner.provides(CategoryCapability.MANAGE_GRANTS));
    assertTrue(owner.provides(CategoryCapability.TRANSFER_OWNERSHIP));
    assertNull(owner.role());
  }

  @Test
  void administrativePermissionsDoNotSupplyOwnershipTransfer() {
    CedarUser administrator = userWithPermissions(
        CedarPermission.WRITE_NOT_WRITABLE_CATEGORY,
        CedarPermission.UPDATE_PERMISSION_NOT_WRITABLE_CATEGORY);
    Set<CategoryCapability> capabilities = CategoryCapabilityPolicy.evaluate(
        new CategoryAuthority(null, false), new CategoryAccessContext(false), administrator);

    assertTrue(capabilities.contains(CategoryCapability.UPDATE_CATEGORY));
    assertTrue(capabilities.contains(CategoryCapability.MANAGE_GRANTS));
    assertFalse(capabilities.contains(CategoryCapability.TRANSFER_OWNERSHIP));
  }

  @ParameterizedTest
  @EnumSource(CategoryCapability.class)
  void rootRestrictionsApplyAfterOwnershipAndAdministrativeAuthority(CategoryCapability capability) {
    CedarUser administrator = userWithPermissions(
        CedarPermission.WRITE_NOT_WRITABLE_CATEGORY,
        CedarPermission.UPDATE_PERMISSION_NOT_WRITABLE_CATEGORY);
    Set<CategoryCapability> capabilities = CategoryCapabilityPolicy.evaluate(
        new CategoryAuthority(null, true), new CategoryAccessContext(true), administrator);
    Set<CategoryCapability> restricted = Set.of(
        CategoryCapability.ATTACH_CATEGORY,
        CategoryCapability.DETACH_CATEGORY,
        CategoryCapability.UPDATE_CATEGORY,
        CategoryCapability.DELETE_CATEGORY,
        CategoryCapability.MOVE_CATEGORY,
        CategoryCapability.TRANSFER_OWNERSHIP);

    assertEquals(!restricted.contains(capability), capabilities.contains(capability));
  }

  @Test
  void currentUserJsonExposesOnlyRoleOwnershipAndCapabilities() throws Exception {
    CurrentUserCategoryPermissions permissions = new CurrentUserCategoryPermissions();
    CategoryAuthority authority = new CategoryAuthority(CategoryRole.EDITOR, false);
    permissions.applyAccess(authority, CategoryCapabilityPolicy.evaluate(
        authority, new CategoryAccessContext(false), new CedarUser()));

    JsonNode json = mapper.readTree(mapper.writeValueAsString(permissions));

    assertEquals("editor", json.get("role").asText());
    assertFalse(json.get("owner").asBoolean());
    assertEquals(Set.of("readCategory", "attachCategory", "detachCategory", "updateCategory",
            "createChildCategory", "deleteCategory"),
        mapper.convertValue(json.get("capabilities"), Set.class));
    assertFalse(json.has("currentUserRole"));
    assertFalse(json.has("canEdit"));
    assertFalse(json.has("canAttach"));
    assertFalse(json.has("canShare"));
    assertFalse(json.has("canWrite"));
  }

  private static CedarUser userWithPermissions(CedarPermission... permissions) {
    CedarUser user = new CedarUser();
    user.setPermissions(Stream.of(permissions).map(CedarPermission::getPermissionName).toList());
    return user;
  }

  private static CategoryPermissions resolvedAcl() {
    CategoryPermissions resolved = new CategoryPermissions();
    resolved.setOwner(new CedarUserExtract(OWNER_ID, "Owner", "User", "owner@example.org"));
    resolved.addUserPermissions(new CategoryUserPermission(
        new CedarUserExtract(USER_ID, "Given", "Family", "user@example.org"), CategoryRole.MANAGER));
    resolved.addGroupPermissions(new CategoryGroupPermission(
        new CedarGroupExtract(GROUP_ID, "Group name"), CategoryRole.CLASSIFIER));
    return resolved;
  }
}
