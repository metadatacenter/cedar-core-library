package org.metadatacenter.server.security.model.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import org.junit.jupiter.api.Test;
import org.metadatacenter.server.security.model.permission.resource.ResourceRole;
import org.metadatacenter.server.security.model.user.CedarGroupExtract;
import org.metadatacenter.server.security.model.user.CedarUserExtract;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Keeps the documented permissions payload in step with the one Jackson serializes.
 *
 * <p>The resource server serves this payload on the permissions routes and the monitor server nests
 * it in its diagnostic reports. Each service hand-wrote a description of it, and the two disagreed
 * on descriptions, on which properties are required and on what the nested user and group
 * references are called. The description now sits on these types, and a property renamed, dropped or
 * added without the annotation following it fails here.</p>
 */
class ResourcePermissionsSchemaTest {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  @Test
  void theDocumentedKeysAreTheKeysJacksonWrites() {
    JsonNode serialized = MAPPER.valueToTree(populatedPermissions());

    assertEquals(Set.of("owner", "userPermissions", "groupPermissions"),
        keysOf(serialized), "keys of the permissions payload");
    assertEquals(Set.of("user", "role"),
        keysOf(serialized.path("userPermissions").path(0)), "keys of a user grant");
    assertEquals(Set.of("group", "role"),
        keysOf(serialized.path("groupPermissions").path(0)), "keys of a group grant");
    assertEquals(Set.of("@id", "firstName", "lastName", "email"),
        keysOf(serialized.path("owner")), "keys of a user reference");
    assertEquals(Set.of("@id", "schema:name"),
        keysOf(serialized.path("groupPermissions").path(0).path("group")),
        "keys of a group reference");

    assertDocuments(CedarNodePermissionsWithExtract.class, serialized);
    assertDocuments(CedarNodeUserPermission.class, serialized.path("userPermissions").path(0));
    assertDocuments(CedarNodeGroupPermission.class, serialized.path("groupPermissions").path(0));
    assertDocuments(CedarUserExtract.class, serialized.path("owner"));
    assertDocuments(CedarGroupExtract.class,
        serialized.path("groupPermissions").path(0).path("group"));
  }

  @Test
  void theRequestConversionStaysOffTheWire() throws Exception {
    assertTrue(Modifier.isPublic(
            CedarNodePermissionsWithExtract.class.getMethod("toRequest").getModifiers()),
        "toRequest is public");

    JsonNode serialized = MAPPER.valueToTree(populatedPermissions());
    assertEquals(Set.of("owner", "userPermissions", "groupPermissions"), keysOf(serialized),
        "toRequest is not a getter, so Jackson writes nothing for it");
  }

  @Test
  void theDocumentedRoleValuesAreTheOnesJacksonWrites() {
    Set<String> written = new LinkedHashSet<>();
    for (ResourceRole role : ResourceRole.values()) {
      written.add(MAPPER.convertValue(role, String.class));
      assertEquals(role, ResourceRole.forValue(role.getValue()), "round trip of " + role);
    }

    assertEquals(Set.of("viewer", "editor", "manager"), written);
    assertEquals("ResourceRole", ResourceRole.class.getAnnotation(Schema.class).name());
    assertTrue(ResourceRole.class.getAnnotation(Schema.class).enumAsRef(),
        "both documents reference the role by name rather than inlining its values");
  }

  @Test
  void theSchemasAreNamedTheSameThingInEveryServiceThatPublishesThem() {
    assertEquals("ResourcePermissions", schemaName(CedarNodePermissionsWithExtract.class));
    assertEquals("ResourceUserGrant", schemaName(CedarNodeUserPermission.class));
    assertEquals("ResourceGroupGrant", schemaName(CedarNodeGroupPermission.class));
    assertEquals("CedarUserReference", schemaName(CedarUserExtract.class));
    assertEquals("CedarGroupReference", schemaName(CedarGroupExtract.class));
  }

  private static CedarNodePermissionsWithExtract populatedPermissions() {
    CedarNodePermissionsWithExtract permissions = new CedarNodePermissionsWithExtract();
    permissions.setOwner(new CedarUserExtract("https://repo.metadatacenter.org/users/1",
        "Test", "User", "test1@test.com"));
    permissions.addUserPermissions(new CedarNodeUserPermission(
        new CedarUserExtract("https://repo.metadatacenter.org/users/2",
            "Other", "User", "test2@test.com"),
        ResourceRole.EDITOR));
    permissions.addGroupPermissions(new CedarNodeGroupPermission(
        new CedarGroupExtract("https://repo.metadatacenter.org/groups/1", "Test group"),
        ResourceRole.MANAGER));
    return permissions;
  }

  private static void assertDocuments(Class<?> documented, JsonNode serialized) {
    Set<String> names = documentedNames(documented);
    for (String key : keysOf(serialized)) {
      assertTrue(names.contains(key),
          documented.getSimpleName() + " writes " + key + " without documenting it");
    }
  }

  private static Set<String> keysOf(JsonNode node) {
    Set<String> keys = new LinkedHashSet<>();
    node.fieldNames().forEachRemaining(keys::add);
    return keys;
  }

  private static Set<String> documentedNames(Class<?> documented) {
    Set<String> names = new LinkedHashSet<>();
    for (Method method : documented.getMethods()) {
      Schema schema = method.getAnnotation(Schema.class);
      if (schema != null && !schema.name().isEmpty()) {
        names.add(schema.name());
      }
    }
    return names;
  }

  private static String schemaName(Class<?> documented) {
    return documented.getAnnotation(Schema.class).name();
  }
}
