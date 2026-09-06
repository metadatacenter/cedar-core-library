package org.metadatacenter.server.security.model.permission;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.metadatacenter.server.security.model.auth.CedarNodeGroupPermission;
import org.metadatacenter.server.security.model.auth.CedarNodePermission;
import org.metadatacenter.server.security.model.auth.CedarNodePermissionsWithExtract;
import org.metadatacenter.server.security.model.auth.CedarNodeUserPermission;
import org.metadatacenter.server.security.model.auth.CurrentUserResourcePermissions;
import org.metadatacenter.model.CedarResourceType;
import org.metadatacenter.server.security.model.permission.resource.ResourceAction;
import org.metadatacenter.server.security.model.permission.resource.ResourceActionPolicy;
import org.metadatacenter.server.security.model.permission.resource.ResourceAccessContext;
import org.metadatacenter.server.security.model.permission.resource.ResourceCapability;
import org.metadatacenter.server.security.model.permission.resource.ResourceCapabilityPolicy;
import org.metadatacenter.server.security.model.permission.resource.ResourceAuthority;
import org.metadatacenter.server.security.model.permission.resource.ResourceRole;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionGroup;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionGroupPermissionPair;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionUser;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionUserPermissionPair;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionsRequest;
import org.metadatacenter.server.security.model.user.CedarGroupExtract;
import org.metadatacenter.server.security.model.user.CedarUserExtract;
import org.metadatacenter.server.security.model.user.CedarUser;
import org.metadatacenter.server.security.model.auth.CedarPermission;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/** Pure model tests for the resolved-resource-ACL to update-request boundary. */
class ResourcePermissionModelTest {

  private static final String OWNER_ID = "https://repo.example/users/owner";
  private static final String USER_ID = "https://repo.example/users/user";
  private static final String GROUP_ID = "https://repo.example/groups/group";
  private final ObjectMapper mapper = new ObjectMapper();

  @ParameterizedTest
  @EnumSource(ResourceRole.class)
  void everyRoleValueRoundTripsThroughTheLookup(ResourceRole role) {
    assertSame(role, ResourceRole.forValue(role.getValue()));
  }

  @Test
  void unknownRoleValueReturnsNull() {
    assertNull(ResourceRole.forValue("admin"));
  }

  @Test
  void nullRoleValueReturnsNull() {
    assertNull(ResourceRole.forValue(null));
  }

  @ParameterizedTest
  @EnumSource(ResourceRole.class)
  void userRoleConvertsToAnIdOnlyRequestPair(ResourceRole role) {
    CedarNodeUserPermission resolved = new CedarNodeUserPermission(
        new CedarUserExtract(USER_ID, "Given", "Family", "user@example.org"), role);

    ResourcePermissionUserPermissionPair pair = resolved.getAsUserIdPermissionPair();

    assertEquals(USER_ID, pair.getUser().getId());
    assertSame(role, pair.getRole());
  }

  @ParameterizedTest
  @EnumSource(ResourceRole.class)
  void groupRoleConvertsToAnIdOnlyRequestPair(ResourceRole role) {
    CedarNodeGroupPermission resolved = new CedarNodeGroupPermission(
        new CedarGroupExtract(GROUP_ID, "Group name"), role);

    ResourcePermissionGroupPermissionPair pair = resolved.getAsGroupIdPermissionPair();

    assertEquals(GROUP_ID, pair.getGroup().getId());
    assertSame(role, pair.getRole());
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
        new ResourcePermissionUser(USER_ID), ResourceRole.MANAGER),
        request.getUserPermissions().get(0));
    assertEquals(new ResourcePermissionGroupPermissionPair(
        new ResourcePermissionGroup(GROUP_ID), ResourceRole.VIEWER),
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
        new CedarUserExtract("user-2", null, null, null), ResourceRole.VIEWER));

    ResourcePermissionsRequest request = resolved.toRequest();

    assertEquals(USER_ID, request.getUserPermissions().get(0).getUser().getId());
    assertEquals("user-2", request.getUserPermissions().get(1).getUser().getId());
  }

  @ParameterizedTest
  @EnumSource(ResourceRole.class)
  void requestJsonRoundTripPreservesEveryRole(ResourceRole role) throws Exception {
    ResourcePermissionsRequest request = new ResourcePermissionsRequest();
    request.setOwner(new ResourcePermissionUser(OWNER_ID));
    request.getUserPermissions().add(new ResourcePermissionUserPermissionPair(
        new ResourcePermissionUser(USER_ID), role));
    request.getGroupPermissions().add(new ResourcePermissionGroupPermissionPair(
        new ResourcePermissionGroup(GROUP_ID), role));

    ResourcePermissionsRequest copy = mapper.readValue(
        mapper.writeValueAsString(request), ResourcePermissionsRequest.class);

    assertEquals(OWNER_ID, copy.getOwner().getId());
    assertSame(role, copy.getUserPermissions().get(0).getRole());
    assertSame(role, copy.getGroupPermissions().get(0).getRole());
  }

  @Test
  void legacyPermissionValuesAreAcceptedButRolesAreEmitted() throws Exception {
    String json = "{\"owner\":{\"@id\":\"" + OWNER_ID + "\"},"
        + "\"userPermissions\":[{\"user\":{\"@id\":\"" + USER_ID
        + "\"},\"permission\":\"write\"}],\"groupPermissions\":[]}";

    ResourcePermissionsRequest request = mapper.readValue(json, ResourcePermissionsRequest.class);

    assertSame(ResourceRole.MANAGER, request.getUserPermissions().get(0).getRole());
    String serialized = mapper.writeValueAsString(request);
    assertTrue(serialized.contains("\"role\":\"manager\""));
    assertFalse(serialized.contains("\"permission\""));
  }

  @Test
  void rolesProvideExactlyTheDocumentedCapabilities() {
    assertTrue(ResourceRole.VIEWER.provides(CedarResourceType.TEMPLATE, ResourceCapability.READ_RESOURCE));
    assertFalse(ResourceRole.VIEWER.provides(CedarResourceType.TEMPLATE, ResourceCapability.UPDATE_RESOURCE));
    assertTrue(ResourceRole.EDITOR.provides(CedarResourceType.TEMPLATE, ResourceCapability.DELETE_RESOURCE));
    assertFalse(ResourceRole.EDITOR.provides(CedarResourceType.TEMPLATE, ResourceCapability.MANAGE_GRANTS));
    assertTrue(ResourceRole.MANAGER.provides(CedarResourceType.TEMPLATE, ResourceCapability.MANAGE_GRANTS));
    assertTrue(ResourceRole.MANAGER.provides(CedarResourceType.TEMPLATE, ResourceCapability.MOVE_RESOURCE));
    assertTrue(ResourceRole.MANAGER.provides(CedarResourceType.TEMPLATE, ResourceCapability.MANAGE_OPENVIEW));
    assertFalse(ResourceRole.EDITOR.provides(CedarResourceType.TEMPLATE, ResourceCapability.CREATE_IN_FOLDER));
    assertTrue(ResourceRole.EDITOR.provides(CedarResourceType.FOLDER, ResourceCapability.MOVE_INTO_FOLDER));
  }

  @Test
  void ownershipAddsTransferWithoutBecomingAnotherRole() {
    ResourceAuthority manager = new ResourceAuthority(ResourceRole.MANAGER, false);
    ResourceAuthority owner = new ResourceAuthority(null, true);

    assertFalse(manager.provides(CedarResourceType.TEMPLATE, ResourceCapability.TRANSFER_OWNERSHIP));
    assertTrue(owner.provides(CedarResourceType.TEMPLATE, ResourceCapability.MANAGE_GRANTS));
    assertTrue(owner.provides(CedarResourceType.TEMPLATE, ResourceCapability.TRANSFER_OWNERSHIP));
  }

  @ParameterizedTest(name = "resource={0}, role={1}, owner={2}")
  @MethodSource("resourceRoleCapabilityMatrix")
  void resourceRoleCapabilityMatrix(CedarResourceType resourceType, ResourceRole role,
                                    boolean owner, Set<ResourceCapability> expectedCapabilities) {
    ResourceAuthority authority = new ResourceAuthority(role, owner);

    assertEquals(expectedCapabilities, authority.capabilitiesFor(resourceType));
  }

  private static Stream<Arguments> resourceRoleCapabilityMatrix() {
    Set<ResourceCapability> noCapabilities = Set.of();
    Set<ResourceCapability> artifactViewer = Set.of(ResourceCapability.READ_RESOURCE);
    Set<ResourceCapability> artifactEditor = Set.of(
        ResourceCapability.READ_RESOURCE,
        ResourceCapability.UPDATE_RESOURCE,
        ResourceCapability.DELETE_RESOURCE);
    Set<ResourceCapability> artifactManager = Set.of(
        ResourceCapability.READ_RESOURCE,
        ResourceCapability.UPDATE_RESOURCE,
        ResourceCapability.DELETE_RESOURCE,
        ResourceCapability.MANAGE_GRANTS,
        ResourceCapability.MOVE_RESOURCE,
        ResourceCapability.MANAGE_OPENVIEW);
    Set<ResourceCapability> artifactOwner = Set.of(
        ResourceCapability.READ_RESOURCE,
        ResourceCapability.UPDATE_RESOURCE,
        ResourceCapability.DELETE_RESOURCE,
        ResourceCapability.MANAGE_GRANTS,
        ResourceCapability.MOVE_RESOURCE,
        ResourceCapability.MANAGE_OPENVIEW,
        ResourceCapability.TRANSFER_OWNERSHIP);
    Set<ResourceCapability> folderViewer = Set.of(
        ResourceCapability.READ_RESOURCE,
        ResourceCapability.LIST_FOLDER_CONTENTS);
    Set<ResourceCapability> folderEditor = Set.of(
        ResourceCapability.READ_RESOURCE,
        ResourceCapability.LIST_FOLDER_CONTENTS,
        ResourceCapability.UPDATE_RESOURCE,
        ResourceCapability.CREATE_IN_FOLDER,
        ResourceCapability.COPY_INTO_FOLDER,
        ResourceCapability.MOVE_INTO_FOLDER,
        ResourceCapability.DELETE_RESOURCE);
    Set<ResourceCapability> folderManager = Set.of(
        ResourceCapability.READ_RESOURCE,
        ResourceCapability.LIST_FOLDER_CONTENTS,
        ResourceCapability.UPDATE_RESOURCE,
        ResourceCapability.CREATE_IN_FOLDER,
        ResourceCapability.COPY_INTO_FOLDER,
        ResourceCapability.MOVE_INTO_FOLDER,
        ResourceCapability.DELETE_RESOURCE,
        ResourceCapability.MANAGE_GRANTS,
        ResourceCapability.MOVE_RESOURCE,
        ResourceCapability.MANAGE_OPENVIEW);
    Set<ResourceCapability> folderOwner = Set.of(
        ResourceCapability.READ_RESOURCE,
        ResourceCapability.LIST_FOLDER_CONTENTS,
        ResourceCapability.UPDATE_RESOURCE,
        ResourceCapability.CREATE_IN_FOLDER,
        ResourceCapability.COPY_INTO_FOLDER,
        ResourceCapability.MOVE_INTO_FOLDER,
        ResourceCapability.DELETE_RESOURCE,
        ResourceCapability.MANAGE_GRANTS,
        ResourceCapability.MOVE_RESOURCE,
        ResourceCapability.MANAGE_OPENVIEW,
        ResourceCapability.TRANSFER_OWNERSHIP);

    Stream.Builder<Arguments> rows = Stream.builder();
    for (CedarResourceType artifactType : List.of(
        CedarResourceType.TEMPLATE, CedarResourceType.ELEMENT,
        CedarResourceType.FIELD, CedarResourceType.INSTANCE)) {
      addAuthorityRows(rows, artifactType, noCapabilities, artifactViewer, artifactEditor,
          artifactManager, artifactOwner);
    }
    addAuthorityRows(rows, CedarResourceType.FOLDER, noCapabilities, folderViewer, folderEditor,
        folderManager, folderOwner);
    return rows.build();
  }

  private static void addAuthorityRows(Stream.Builder<Arguments> rows,
                                       CedarResourceType resourceType,
                                       Set<ResourceCapability> noCapabilities,
                                       Set<ResourceCapability> viewerCapabilities,
                                       Set<ResourceCapability> editorCapabilities,
                                       Set<ResourceCapability> managerCapabilities,
                                       Set<ResourceCapability> ownerCapabilities) {
    rows.add(Arguments.of(resourceType, null, false, noCapabilities));
    rows.add(Arguments.of(resourceType, ResourceRole.VIEWER, false, viewerCapabilities));
    rows.add(Arguments.of(resourceType, ResourceRole.EDITOR, false, editorCapabilities));
    rows.add(Arguments.of(resourceType, ResourceRole.MANAGER, false, managerCapabilities));
    rows.add(Arguments.of(resourceType, null, true, ownerCapabilities));
  }

  @ParameterizedTest(name = "resource={0}, role={1}, owner={2}, open={3}")
  @MethodSource("authorityAndStateActionCases")
  void effectiveCapabilitiesAndResourceStateDetermineAvailableActions(
      CedarResourceType resourceType, ResourceRole role, boolean owner, boolean open,
      Set<ResourceAction> expectedActions) {
    ResourceAuthority authority = new ResourceAuthority(role, owner);
    Set<ResourceCapability> capabilities = authority.capabilitiesFor(resourceType);

    assertEquals(expectedActions, ResourceActionPolicy.evaluate(resourceType, capabilities, open));
  }

  private static Stream<Arguments> authorityAndStateActionCases() {
    Stream.Builder<Arguments> rows = Stream.builder();
    for (CedarResourceType resourceType : List.of(
        CedarResourceType.TEMPLATE, CedarResourceType.ELEMENT,
        CedarResourceType.FIELD, CedarResourceType.INSTANCE, CedarResourceType.FOLDER)) {
      for (ResourceRole role : ResourceRole.values()) {
        addActionRows(rows, resourceType, role, false);
      }
      addActionRows(rows, resourceType, null, false);
      addActionRows(rows, resourceType, null, true);
    }
    return rows.build();
  }

  private static void addActionRows(Stream.Builder<Arguments> rows,
                                    CedarResourceType resourceType,
                                    ResourceRole role,
                                    boolean owner) {
    boolean readable = owner || role != null;
    boolean canManageOpenView = owner || role == ResourceRole.MANAGER;
    EnumSet<ResourceAction> closedActions = EnumSet.noneOf(ResourceAction.class);
    EnumSet<ResourceAction> openActions = EnumSet.noneOf(ResourceAction.class);
    if (readable && resourceType != CedarResourceType.FOLDER) {
      closedActions.add(ResourceAction.COPY_FROM_RESOURCE);
      openActions.add(ResourceAction.COPY_FROM_RESOURCE);
    }
    if (readable && resourceType == CedarResourceType.TEMPLATE) {
      closedActions.add(ResourceAction.POPULATE);
      openActions.add(ResourceAction.POPULATE);
    }
    if (canManageOpenView) {
      closedActions.add(ResourceAction.ENABLE_OPENVIEW);
      openActions.add(ResourceAction.DISABLE_OPENVIEW);
    }
    rows.add(Arguments.of(resourceType, role, owner, false, Set.copyOf(closedActions)));
    rows.add(Arguments.of(resourceType, role, owner, true, Set.copyOf(openActions)));
  }

  @Test
  void currentUserPermissionJsonExposesTheNewRoleAndCapabilityContract() throws Exception {
    CurrentUserResourcePermissions permissions = new CurrentUserResourcePermissions();
    permissions.applyAuthority(
        new ResourceAuthority(ResourceRole.EDITOR, false), CedarResourceType.TEMPLATE);
    permissions.setActionAvailable(ResourceAction.COPY_FROM_RESOURCE, true);
    permissions.setActionAvailable(ResourceAction.POPULATE, true);

    JsonNode json = mapper.readTree(mapper.writeValueAsString(permissions));

    assertEquals("editor", json.get("role").asText());
    assertEquals("editor", json.get("currentUserRole").asText());
    assertFalse(json.get("owner").asBoolean());
    assertEquals(Set.of("readResource", "updateResource", "deleteResource"),
        mapper.convertValue(json.get("capabilities"), Set.class));
    assertEquals(Set.of("copyFromResource", "populate"),
        mapper.convertValue(json.get("availableActions"), Set.class));
    assertTrue(json.get("canEdit").asBoolean());
    assertFalse(json.get("canCreate").asBoolean());
    assertFalse(json.get("canWrite").asBoolean(), "Editor is not legacy WRITE");
    assertTrue(json.get("canCopy").asBoolean());
  }

  @Test
  void managerAndOwnerContinueToAppearWritableToLegacyClients() {
    for (ResourceAuthority authority : List.of(
        new ResourceAuthority(ResourceRole.MANAGER, false),
        new ResourceAuthority(null, true))) {
      CurrentUserResourcePermissions permissions = new CurrentUserResourcePermissions();
      permissions.applyAuthority(authority, CedarResourceType.TEMPLATE);
      assertTrue(permissions.isCanWrite());
    }
  }

  @Test
  void ownershipIsReportedSeparatelyFromRole() throws Exception {
    CurrentUserResourcePermissions permissions = new CurrentUserResourcePermissions();
    permissions.applyAuthority(new ResourceAuthority(null, true), CedarResourceType.TEMPLATE);

    JsonNode json = mapper.readTree(mapper.writeValueAsString(permissions));

    assertTrue(json.get("owner").asBoolean());
    assertTrue(json.get("role").isNull());
    assertTrue(json.get("currentUserRole").isNull());
    assertTrue(json.get("capabilities").toString().contains("transferOwnership"));
  }

  @Test
  void administrativePermissionsAddCapabilitiesWithoutInventingAResourceRole() {
    CedarUser administrator = userWithPermissions(
        CedarPermission.READ_NOT_READABLE_NODE,
        CedarPermission.WRITE_NOT_WRITABLE_NODE,
        CedarPermission.UPDATE_PERMISSION_NOT_WRITABLE_NODE);
    ResourceAuthority noGrant = new ResourceAuthority(null, false);

    Set<ResourceCapability> capabilities = ResourceCapabilityPolicy.evaluate(noGrant,
        ResourceAccessContext.ordinary(CedarResourceType.TEMPLATE), administrator);

    assertTrue(capabilities.contains(ResourceCapability.READ_RESOURCE));
    assertTrue(capabilities.contains(ResourceCapability.UPDATE_RESOURCE));
    assertTrue(capabilities.contains(ResourceCapability.MANAGE_GRANTS));
    assertFalse(capabilities.contains(ResourceCapability.TRANSFER_OWNERSHIP));
    assertNull(noGrant.role());
  }

  @ParameterizedTest
  @EnumSource(ResourceCapability.class)
  void protectedFoldersApplyTheSameRestrictionsToProjectionAndEnforcement(ResourceCapability capability) {
    ResourceAuthority owner = new ResourceAuthority(null, true);
    Set<ResourceCapability> protectedCapabilities = ResourceCapabilityPolicy.evaluate(owner,
        new ResourceAccessContext(CedarResourceType.FOLDER, true), new CedarUser());
    boolean expected = !Set.of(
        ResourceCapability.DELETE_RESOURCE,
        ResourceCapability.MANAGE_GRANTS,
        ResourceCapability.MOVE_RESOURCE,
        ResourceCapability.MANAGE_OPENVIEW,
        ResourceCapability.TRANSFER_OWNERSHIP).contains(capability)
        && capability.appliesTo(CedarResourceType.FOLDER);
    assertEquals(expected, protectedCapabilities.contains(capability), capability.getValue());
  }

  private static CedarUser userWithPermissions(CedarPermission... permissions) {
    CedarUser user = new CedarUser();
    user.setPermissions(Stream.of(permissions).map(CedarPermission::getPermissionName).toList());
    return user;
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
        ResourceRole.MANAGER));
    resolved.addGroupPermissions(new CedarNodeGroupPermission(
        new CedarGroupExtract(GROUP_ID, "Group name"), ResourceRole.VIEWER));
    return resolved;
  }
}
