package org.metadatacenter.server.security.model.permission;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.metadatacenter.server.security.model.auth.CedarNodeGroupPermission;
import org.metadatacenter.server.security.model.auth.CedarNodeMaterializedPermissions;
import org.metadatacenter.server.security.model.auth.CedarNodePermission;
import org.metadatacenter.server.security.model.auth.CedarNodePermissionsWithExtract;
import org.metadatacenter.server.security.model.auth.CedarNodeUserPermission;
import org.metadatacenter.server.security.model.auth.CurrentUserResourcePermissions;
import org.metadatacenter.server.security.model.auth.NodeSharePermission;
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
    assertNull(ResourceRole.forValue("read"));
    assertNull(ResourceRole.forValue("write"));
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

  /**
   * The search index stores each grant as one of these keys and queries for the same keys, so the
   * format is stored data: a change hides every indexed grant until the index is rebuilt.
   */
  @ParameterizedTest
  @CsvSource({"VIEWER,viewer", "EDITOR,editor", "MANAGER,manager"})
  void materializedGrantKeysJoinThePrincipalAndTheRoleValue(ResourceRole role, String storedRole) {
    String key = CedarNodeMaterializedPermissions.getKey(USER_ID, role);

    assertEquals(USER_ID + "|" + storedRole, key);
    assertEquals(USER_ID, CedarNodePermission.getId(key));
  }

  /**
   * The graph and the search index store the everybody group's share as "read", "write" or nothing,
   * and search widens a user's role from the value it reads back.
   */
  @Test
  void everybodyShareValuesReadBackAsTheirPermission() throws Exception {
    assertSame(NodeSharePermission.READ, mapper.readValue("\"read\"", NodeSharePermission.class));
    assertSame(NodeSharePermission.WRITE, mapper.readValue("\"write\"", NodeSharePermission.class));
    assertSame(NodeSharePermission.NONE, mapper.readValue("\"viewer\"", NodeSharePermission.class));
    assertSame(NodeSharePermission.NONE, NodeSharePermission.forValue(null));
    assertEquals("\"read\"", mapper.writeValueAsString(NodeSharePermission.READ));
    assertEquals("\"write\"", mapper.writeValueAsString(NodeSharePermission.WRITE));
    assertEquals("null", mapper.writeValueAsString(NodeSharePermission.NONE));
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
  void legacyPermissionPropertyIsRejected() {
    String json = "{\"owner\":{\"@id\":\"" + OWNER_ID + "\"},"
        + "\"userPermissions\":[{\"user\":{\"@id\":\"" + USER_ID
        + "\"},\"permission\":\"write\"}],\"groupPermissions\":[]}";

    assertThrows(UnrecognizedPropertyException.class,
        () -> mapper.readValue(json, ResourcePermissionsRequest.class));
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

  /**
   * Search turns a required role into the grant keys that satisfy it through {@code includes}, and the
   * search index and the graph's grant reader keep the stronger of two grants through {@code strongest}.
   */
  @Test
  void strongerRolesIncludeWeakerOnesAndWinWhicheverComesFirst() {
    List<ResourceRole> weakestFirst = List.of(ResourceRole.VIEWER, ResourceRole.EDITOR, ResourceRole.MANAGER);
    assertEquals(EnumSet.allOf(ResourceRole.class), EnumSet.copyOf(weakestFirst));

    for (ResourceRole held : weakestFirst) {
      for (ResourceRole other : weakestFirst) {
        boolean atLeastAsStrong = weakestFirst.indexOf(held) >= weakestFirst.indexOf(other);
        ResourceRole stronger = atLeastAsStrong ? held : other;
        assertEquals(atLeastAsStrong, held.includes(other), held + " includes " + other);
        assertSame(stronger, ResourceRole.strongest(held, other));
        assertSame(stronger, ResourceRole.strongest(other, held));
      }
      assertFalse(held.includes(null));
      assertSame(held, ResourceRole.strongest(null, held));
      assertSame(held, ResourceRole.strongest(held, null));
    }
    assertNull(ResourceRole.strongest(null, null));
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
    assertFalse(json.get("owner").asBoolean());
    assertEquals(Set.of("readResource", "updateResource", "deleteResource"),
        mapper.convertValue(json.get("capabilities"), Set.class));
    assertEquals(Set.of("copyFromResource", "populate"),
        mapper.convertValue(json.get("availableActions"), Set.class));
    assertFalse(json.has("currentUserRole"));
    assertFalse(json.has("canEdit"));
    assertFalse(json.has("canCreate"));
    assertFalse(json.has("canWrite"));
    assertFalse(json.has("canCopy"));
  }

  /**
   * The permission updaters pass the capabilities the policy settled on, which administrative
   * permissions can widen beyond the role. The user is shown those, beside the role they hold.
   */
  @Test
  void appliedAccessReportsTheEffectiveCapabilitiesBesideTheHeldRole() {
    CurrentUserResourcePermissions permissions = new CurrentUserResourcePermissions();
    Set<ResourceCapability> effective = EnumSet.of(
        ResourceCapability.READ_RESOURCE, ResourceCapability.MANAGE_GRANTS);

    permissions.applyAccess(new ResourceAuthority(ResourceRole.VIEWER, false), effective);
    effective.add(ResourceCapability.DELETE_RESOURCE);

    assertSame(ResourceRole.VIEWER, permissions.getRole());
    assertFalse(permissions.isOwner());
    assertEquals(Set.of(ResourceCapability.READ_RESOURCE, ResourceCapability.MANAGE_GRANTS),
        permissions.getCapabilities(), "a later change to the caller's set does not reach the record");
    assertThrows(UnsupportedOperationException.class,
        () -> permissions.getCapabilities().add(ResourceCapability.DELETE_RESOURCE));
  }

  /** The resource server refuses to publish or to create a draft unless that action was made available. */
  @Test
  void theVersionCommandsAreAllowedOnlyWhileTheirActionIsAvailable() {
    CurrentUserResourcePermissions permissions = new CurrentUserResourcePermissions();
    assertFalse(permissions.isCanPublish());
    assertFalse(permissions.isCanCreateDraft());

    permissions.setActionAvailable(ResourceAction.PUBLISH, true);
    assertTrue(permissions.isCanPublish());
    assertFalse(permissions.isCanCreateDraft());

    permissions.setActionAvailable(ResourceAction.CREATE_DRAFT, true);
    permissions.setActionAvailable(ResourceAction.PUBLISH, false);
    assertFalse(permissions.isCanPublish());
    assertTrue(permissions.isCanCreateDraft());
    assertEquals(Set.of(ResourceAction.CREATE_DRAFT), permissions.getAvailableActions());
  }

  @Test
  void ownershipIsReportedSeparatelyFromRole() throws Exception {
    CurrentUserResourcePermissions permissions = new CurrentUserResourcePermissions();
    permissions.applyAuthority(new ResourceAuthority(null, true), CedarResourceType.TEMPLATE);

    JsonNode json = mapper.readTree(mapper.writeValueAsString(permissions));

    assertTrue(json.get("owner").asBoolean());
    assertTrue(json.get("role").isNull());
    assertFalse(json.has("currentUserRole"));
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

  /**
   * Administrative permissions on folders, ordinary and protected. Root, system and home folders
   * all reach the policy as a protected folder, and the restrictions must still apply after the
   * administrative capabilities are added.
   */
  @ParameterizedTest(name = "resource={0}, protected={1}, owner={2}, permissions={3}")
  @MethodSource("administrativeCapabilityMatrix")
  void administrativeCapabilityMatrix(CedarResourceType resourceType, boolean protectedFolder,
                                      boolean owner, Set<CedarPermission> permissions,
                                      Set<ResourceCapability> expectedCapabilities) {
    CedarUser administrator = userWithPermissions(permissions.toArray(CedarPermission[]::new));

    Set<ResourceCapability> capabilities = ResourceCapabilityPolicy.evaluate(
        new ResourceAuthority(null, owner),
        new ResourceAccessContext(resourceType, protectedFolder), administrator);

    assertEquals(expectedCapabilities, capabilities);
  }

  private static Stream<Arguments> administrativeCapabilityMatrix() {
    Set<CedarPermission> read = Set.of(CedarPermission.READ_NOT_READABLE_NODE);
    Set<CedarPermission> write = Set.of(CedarPermission.WRITE_NOT_WRITABLE_NODE);
    Set<CedarPermission> grants = Set.of(CedarPermission.UPDATE_PERMISSION_NOT_WRITABLE_NODE);
    Set<CedarPermission> all = Set.of(
        CedarPermission.READ_NOT_READABLE_NODE,
        CedarPermission.WRITE_NOT_WRITABLE_NODE,
        CedarPermission.UPDATE_PERMISSION_NOT_WRITABLE_NODE);
    Set<ResourceCapability> folderRead = Set.of(
        ResourceCapability.READ_RESOURCE,
        ResourceCapability.LIST_FOLDER_CONTENTS);
    Set<ResourceCapability> protectedFolderWrite = Set.of(
        ResourceCapability.UPDATE_RESOURCE,
        ResourceCapability.CREATE_IN_FOLDER,
        ResourceCapability.COPY_INTO_FOLDER,
        ResourceCapability.MOVE_INTO_FOLDER);
    Set<ResourceCapability> folderWrite = Set.of(
        ResourceCapability.UPDATE_RESOURCE,
        ResourceCapability.CREATE_IN_FOLDER,
        ResourceCapability.COPY_INTO_FOLDER,
        ResourceCapability.MOVE_INTO_FOLDER,
        ResourceCapability.DELETE_RESOURCE,
        ResourceCapability.MOVE_RESOURCE,
        ResourceCapability.MANAGE_OPENVIEW);
    Set<ResourceCapability> protectedFolderAll = Set.of(
        ResourceCapability.READ_RESOURCE,
        ResourceCapability.LIST_FOLDER_CONTENTS,
        ResourceCapability.UPDATE_RESOURCE,
        ResourceCapability.CREATE_IN_FOLDER,
        ResourceCapability.COPY_INTO_FOLDER,
        ResourceCapability.MOVE_INTO_FOLDER);
    Set<ResourceCapability> folderAll = Set.of(
        ResourceCapability.READ_RESOURCE,
        ResourceCapability.LIST_FOLDER_CONTENTS,
        ResourceCapability.UPDATE_RESOURCE,
        ResourceCapability.CREATE_IN_FOLDER,
        ResourceCapability.COPY_INTO_FOLDER,
        ResourceCapability.MOVE_INTO_FOLDER,
        ResourceCapability.DELETE_RESOURCE,
        ResourceCapability.MOVE_RESOURCE,
        ResourceCapability.MANAGE_OPENVIEW,
        ResourceCapability.MANAGE_GRANTS);
    Set<ResourceCapability> templateAll = Set.of(
        ResourceCapability.READ_RESOURCE,
        ResourceCapability.UPDATE_RESOURCE,
        ResourceCapability.DELETE_RESOURCE,
        ResourceCapability.MOVE_RESOURCE,
        ResourceCapability.MANAGE_OPENVIEW,
        ResourceCapability.MANAGE_GRANTS);
    CedarResourceType folder = CedarResourceType.FOLDER;
    return Stream.of(
        Arguments.of(CedarResourceType.TEMPLATE, false, false, read,
            Set.of(ResourceCapability.READ_RESOURCE)),
        Arguments.of(CedarResourceType.TEMPLATE, false, false, write, Set.of(
            ResourceCapability.UPDATE_RESOURCE,
            ResourceCapability.DELETE_RESOURCE,
            ResourceCapability.MOVE_RESOURCE,
            ResourceCapability.MANAGE_OPENVIEW)),
        Arguments.of(CedarResourceType.TEMPLATE, false, false, grants,
            Set.of(ResourceCapability.MANAGE_GRANTS)),
        Arguments.of(CedarResourceType.TEMPLATE, false, false, all, templateAll),
        Arguments.of(folder, false, false, read, folderRead),
        Arguments.of(folder, false, false, write, folderWrite),
        Arguments.of(folder, false, false, grants, Set.of(ResourceCapability.MANAGE_GRANTS)),
        Arguments.of(folder, false, false, all, folderAll),
        Arguments.of(folder, true, false, read, folderRead),
        Arguments.of(folder, true, false, write, protectedFolderWrite),
        Arguments.of(folder, true, false, grants, Set.of()),
        Arguments.of(folder, true, false, all, protectedFolderAll),
        Arguments.of(folder, true, true, all, protectedFolderAll));
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
  void requestJsonRefusesUnknownProperties() {
    String json = "{\"owner\":{\"@id\":\"" + OWNER_ID + "\",\"label\":\"ignored\"},"
        + "\"userPermissions\":[],\"groupPermissions\":[],\"futureField\":true}";

    assertThrows(UnrecognizedPropertyException.class,
        () -> mapper.readValue(json, ResourcePermissionsRequest.class),
        "a permission request is a body CEDAR owns, and carries only what it declares");
  }

  @Test
  void requestJsonReadsWhatItDeclares() throws Exception {
    String json = "{\"owner\":{\"@id\":\"" + OWNER_ID + "\"},"
        + "\"userPermissions\":[],\"groupPermissions\":[]}";

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
