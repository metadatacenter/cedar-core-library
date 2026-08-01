package org.metadatacenter.server.security.model.permission;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.metadatacenter.server.security.model.auth.CedarNodeGroupPermission;
import org.metadatacenter.server.security.model.auth.CedarNodePermission;
import org.metadatacenter.server.security.model.auth.CedarNodePermissionsWithExtract;
import org.metadatacenter.server.security.model.auth.CedarNodeUserPermission;
import org.metadatacenter.server.security.model.permission.resource.FilesystemResourcePermission;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionGroup;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionGroupPermissionPair;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionUser;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionUserPermissionPair;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionsRequest;
import org.metadatacenter.server.security.model.user.CedarGroupExtract;
import org.metadatacenter.server.security.model.user.CedarUserExtract;

import static org.junit.jupiter.api.Assertions.*;

/** Pure model tests for the resolved-resource-ACL to update-request boundary. */
class ResourcePermissionModelTest {

  private static final String OWNER_ID = "https://repo.example/users/owner";
  private static final String USER_ID = "https://repo.example/users/user";
  private static final String GROUP_ID = "https://repo.example/groups/group";
  private final ObjectMapper mapper = new ObjectMapper();

  @ParameterizedTest
  @EnumSource(FilesystemResourcePermission.class)
  void everyPermissionValueRoundTripsThroughTheLookup(FilesystemResourcePermission permission) {
    assertSame(permission, FilesystemResourcePermission.forValue(permission.getValue()));
  }

  @Test
  void unknownPermissionValueReturnsNull() {
    assertNull(FilesystemResourcePermission.forValue("admin"));
  }

  @Test
  void nullPermissionValueReturnsNull() {
    assertNull(FilesystemResourcePermission.forValue(null));
  }

  @ParameterizedTest
  @EnumSource(FilesystemResourcePermission.class)
  void userPermissionConvertsToAnIdOnlyRequestPair(FilesystemResourcePermission permission) {
    CedarNodeUserPermission resolved = new CedarNodeUserPermission(
        new CedarUserExtract(USER_ID, "Given", "Family", "user@example.org"), permission);

    ResourcePermissionUserPermissionPair pair = resolved.getAsUserIdPermissionPair();

    assertEquals(USER_ID, pair.getUser().getId());
    assertSame(permission, pair.getPermission());
  }

  @ParameterizedTest
  @EnumSource(FilesystemResourcePermission.class)
  void groupPermissionConvertsToAnIdOnlyRequestPair(FilesystemResourcePermission permission) {
    CedarNodeGroupPermission resolved = new CedarNodeGroupPermission(
        new CedarGroupExtract(GROUP_ID, "Group name"), permission);

    ResourcePermissionGroupPermissionPair pair = resolved.getAsGroupIdPermissionPair();

    assertEquals(GROUP_ID, pair.getGroup().getId());
    assertSame(permission, pair.getPermission());
  }

  @ParameterizedTest
  @CsvSource(value = {
      "user-1|read,user-1",
      "user-1|read|extra,user-1",
      "|write,''",
      "plain,<null>",
      "<null>,<null>"
  }, nullValues = "<null>")
  void compositeKeyIdExtractionIsStable(String key, String expectedId) {
    assertEquals(expectedId, CedarNodePermission.getId(key));
  }

  @Test
  void resolvedAclConvertsOwnerUsersAndGroupsToARequest() {
    CedarNodePermissionsWithExtract resolved = resolvedAcl();

    ResourcePermissionsRequest request = resolved.toRequest();

    assertEquals(OWNER_ID, request.getOwner().getId());
    assertEquals(new ResourcePermissionUserPermissionPair(
        new ResourcePermissionUser(USER_ID), FilesystemResourcePermission.WRITE),
        request.getUserPermissions().get(0));
    assertEquals(new ResourcePermissionGroupPermissionPair(
        new ResourcePermissionGroup(GROUP_ID), FilesystemResourcePermission.READ),
        request.getGroupPermissions().get(0));
  }

  @Test
  void conversionProducesDetachedRequestIdentityObjects() {
    CedarNodePermissionsWithExtract resolved = resolvedAcl();
    ResourcePermissionsRequest request = resolved.toRequest();

    resolved.getOwner().setId("changed-owner");
    resolved.getUserPermissions().get(0).getUser().setId("changed-user");
    resolved.getGroupPermissions().get(0).getGroup().setId("changed-group");

    assertEquals(OWNER_ID, request.getOwner().getId());
    assertEquals(USER_ID, request.getUserPermissions().get(0).getUser().getId());
    assertEquals(GROUP_ID, request.getGroupPermissions().get(0).getGroup().getId());
  }

  @Test
  void conversionPreservesGrantInsertionOrder() {
    CedarNodePermissionsWithExtract resolved = resolvedAcl();
    resolved.addUserPermissions(new CedarNodeUserPermission(
        new CedarUserExtract("user-2", null, null, null), FilesystemResourcePermission.READ));

    ResourcePermissionsRequest request = resolved.toRequest();

    assertEquals(USER_ID, request.getUserPermissions().get(0).getUser().getId());
    assertEquals("user-2", request.getUserPermissions().get(1).getUser().getId());
  }

  @ParameterizedTest
  @EnumSource(FilesystemResourcePermission.class)
  void requestJsonRoundTripPreservesEveryPermission(FilesystemResourcePermission permission) throws Exception {
    ResourcePermissionsRequest request = new ResourcePermissionsRequest();
    request.setOwner(new ResourcePermissionUser(OWNER_ID));
    request.getUserPermissions().add(new ResourcePermissionUserPermissionPair(
        new ResourcePermissionUser(USER_ID), permission));
    request.getGroupPermissions().add(new ResourcePermissionGroupPermissionPair(
        new ResourcePermissionGroup(GROUP_ID), permission));

    ResourcePermissionsRequest copy = mapper.readValue(
        mapper.writeValueAsString(request), ResourcePermissionsRequest.class);

    assertEquals(OWNER_ID, copy.getOwner().getId());
    assertSame(permission, copy.getUserPermissions().get(0).getPermission());
    assertSame(permission, copy.getGroupPermissions().get(0).getPermission());
  }

  @Test
  void requestJsonIgnoresUnknownProperties() throws Exception {
    String json = "{\"owner\":{\"@id\":\"" + OWNER_ID + "\",\"label\":\"ignored\"},"
        + "\"userPermissions\":[],\"groupPermissions\":[],\"futureField\":true}";

    ResourcePermissionsRequest request = mapper.readValue(json, ResourcePermissionsRequest.class);

    assertEquals(OWNER_ID, request.getOwner().getId());
    assertTrue(request.getUserPermissions().isEmpty());
    assertTrue(request.getGroupPermissions().isEmpty());
  }

  private static CedarNodePermissionsWithExtract resolvedAcl() {
    CedarNodePermissionsWithExtract resolved = new CedarNodePermissionsWithExtract();
    resolved.setOwner(new CedarUserExtract(OWNER_ID, "Owner", "User", "owner@example.org"));
    resolved.addUserPermissions(new CedarNodeUserPermission(
        new CedarUserExtract(USER_ID, "Given", "Family", "user@example.org"),
        FilesystemResourcePermission.WRITE));
    resolved.addGroupPermissions(new CedarNodeGroupPermission(
        new CedarGroupExtract(GROUP_ID, "Group name"), FilesystemResourcePermission.READ));
    return resolved;
  }
}
