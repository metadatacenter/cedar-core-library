package org.metadatacenter.server.security.model.permission;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.metadatacenter.server.security.model.permission.category.*;
import org.metadatacenter.server.security.model.user.CedarGroupExtract;
import org.metadatacenter.server.security.model.user.CedarUserExtract;

import static org.junit.jupiter.api.Assertions.*;

/** Pure model tests for the resolved-category-ACL to update-request boundary. */
class CategoryPermissionModelTest {

  private static final String OWNER_ID = "https://repo.example/users/owner";
  private static final String USER_ID = "https://repo.example/users/user";
  private static final String GROUP_ID = "https://repo.example/groups/group";
  private final ObjectMapper mapper = new ObjectMapper();

  @ParameterizedTest
  @EnumSource(CategoryPermission.class)
  void everyPermissionValueRoundTripsThroughTheLookup(CategoryPermission permission) {
    assertSame(permission, CategoryPermission.forValue(permission.getValue()));
  }

  @Test
  void unknownPermissionValueReturnsNull() {
    assertNull(CategoryPermission.forValue("read"));
  }

  @Test
  void nullPermissionValueReturnsNull() {
    assertNull(CategoryPermission.forValue(null));
  }

  @ParameterizedTest
  @EnumSource(CategoryPermission.class)
  void userPermissionConvertsToAnIdOnlyRequestPair(CategoryPermission permission) {
    CategoryUserPermission resolved = new CategoryUserPermission(
        new CedarUserExtract(USER_ID, "Given", "Family", "user@example.org"), permission);

    CategoryPermissionUserPermissionPair pair = resolved.getAsUserIdPermissionPair();

    assertEquals(USER_ID, pair.getUser().getId());
    assertSame(permission, pair.getPermission());
  }

  @ParameterizedTest
  @EnumSource(CategoryPermission.class)
  void groupPermissionConvertsToAnIdOnlyRequestPair(CategoryPermission permission) {
    CategoryGroupPermission resolved = new CategoryGroupPermission(
        new CedarGroupExtract(GROUP_ID, "Group name"), permission);

    CategoryPermissionGroupPermissionPair pair = resolved.getAsGroupIdPermissionPair();

    assertEquals(GROUP_ID, pair.getGroup().getId());
    assertSame(permission, pair.getPermission());
  }

  @ParameterizedTest
  @CsvSource(value = {
      "group-1|attach,group-1",
      "group-1|attach|extra,group-1",
      "|write,''",
      "plain,<null>",
      "<null>,<null>"
  }, nullValues = "<null>")
  void compositeKeyIdExtractionIsStable(String key, String expectedId) {
    assertEquals(expectedId, AbstractCategoryPermission.getId(key));
  }

  @Test
  void resolvedAclConvertsOwnerUsersAndGroupsToARequest() {
    CategoryPermissions resolved = resolvedAcl();

    CategoryPermissionRequest request = resolved.toRequest();

    assertEquals(OWNER_ID, request.getOwner().getId());
    assertEquals(new CategoryPermissionUserPermissionPair(
        new CategoryPermissionUser(USER_ID), CategoryPermission.WRITE), request.getUserPermissions().get(0));
    assertEquals(new CategoryPermissionGroupPermissionPair(
        new CategoryPermissionGroup(GROUP_ID), CategoryPermission.ATTACH), request.getGroupPermissions().get(0));
  }

  @Test
  void conversionProducesDetachedRequestIdentityObjects() {
    CategoryPermissions resolved = resolvedAcl();
    CategoryPermissionRequest request = resolved.toRequest();

    resolved.getOwner().setId("changed-owner");
    resolved.getUserPermissions().get(0).getUser().setId("changed-user");
    resolved.getGroupPermissions().get(0).getGroup().setId("changed-group");

    assertEquals(OWNER_ID, request.getOwner().getId());
    assertEquals(USER_ID, request.getUserPermissions().get(0).getUser().getId());
    assertEquals(GROUP_ID, request.getGroupPermissions().get(0).getGroup().getId());
  }

  @ParameterizedTest
  @EnumSource(CategoryPermission.class)
  void requestJsonRoundTripPreservesEveryPermission(CategoryPermission permission) throws Exception {
    CategoryPermissionRequest request = new CategoryPermissionRequest();
    request.setOwner(new CategoryPermissionUser(OWNER_ID));
    request.getUserPermissions().add(new CategoryPermissionUserPermissionPair(
        new CategoryPermissionUser(USER_ID), permission));
    request.getGroupPermissions().add(new CategoryPermissionGroupPermissionPair(
        new CategoryPermissionGroup(GROUP_ID), permission));

    CategoryPermissionRequest copy = mapper.readValue(
        mapper.writeValueAsString(request), CategoryPermissionRequest.class);

    assertEquals(OWNER_ID, copy.getOwner().getId());
    assertSame(permission, copy.getUserPermissions().get(0).getPermission());
    assertSame(permission, copy.getGroupPermissions().get(0).getPermission());
  }

  @Test
  void requestJsonIgnoresUnknownProperties() throws Exception {
    String json = "{\"owner\":{\"@id\":\"" + OWNER_ID + "\",\"label\":\"ignored\"},"
        + "\"userPermissions\":[],\"groupPermissions\":[],\"futureField\":true}";

    CategoryPermissionRequest request = mapper.readValue(json, CategoryPermissionRequest.class);

    assertEquals(OWNER_ID, request.getOwner().getId());
    assertTrue(request.getUserPermissions().isEmpty());
    assertTrue(request.getGroupPermissions().isEmpty());
  }

  @Test
  void distinctCategoryGroupsWithTheSameIdAreEqual() {
    CategoryPermissionGroup left = new CategoryPermissionGroup(GROUP_ID);
    CategoryPermissionGroup right = new CategoryPermissionGroup(GROUP_ID);

    assertEquals(left, right);
    assertEquals(left.hashCode(), right.hashCode());
  }

  @Test
  void categoryGroupsWithDifferentIdsAreNotEqual() {
    assertNotEquals(new CategoryPermissionGroup(GROUP_ID), new CategoryPermissionGroup("another-group"));
  }

  @Test
  void categoryGroupPairsHaveValueEquality() {
    CategoryPermissionGroupPermissionPair left = new CategoryPermissionGroupPermissionPair(
        new CategoryPermissionGroup(GROUP_ID), CategoryPermission.ATTACH);
    CategoryPermissionGroupPermissionPair right = new CategoryPermissionGroupPermissionPair(
        new CategoryPermissionGroup(GROUP_ID), CategoryPermission.ATTACH);

    assertEquals(left, right);
    assertEquals(left.hashCode(), right.hashCode());
  }

  private static CategoryPermissions resolvedAcl() {
    CategoryPermissions resolved = new CategoryPermissions();
    resolved.setOwner(new CedarUserExtract(OWNER_ID, "Owner", "User", "owner@example.org"));
    resolved.addUserPermissions(new CategoryUserPermission(
        new CedarUserExtract(USER_ID, "Given", "Family", "user@example.org"), CategoryPermission.WRITE));
    resolved.addGroupPermissions(new CategoryGroupPermission(
        new CedarGroupExtract(GROUP_ID, "Group name"), CategoryPermission.ATTACH));
    return resolved;
  }
}
