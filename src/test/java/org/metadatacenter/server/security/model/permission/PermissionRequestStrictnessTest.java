package org.metadatacenter.server.security.model.permission;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.metadatacenter.server.security.model.auth.CedarGroupUserRequest;
import org.metadatacenter.server.security.model.permission.category.CategoryPermissionGroup;
import org.metadatacenter.server.security.model.permission.category.CategoryPermissionGroupPermissionPair;
import org.metadatacenter.server.security.model.permission.category.CategoryPermissionRequest;
import org.metadatacenter.server.security.model.permission.category.CategoryPermissionUser;
import org.metadatacenter.server.security.model.permission.category.CategoryPermissionUserPermissionPair;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionGroup;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionGroupPermissionPair;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionUser;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionUserPermissionPair;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionsRequest;
import org.metadatacenter.util.json.JsonMapper;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A permission request is a body CEDAR owns, and it is read strictly.
 *
 * <p>These types used to carry {@code @JsonIgnoreProperties(ignoreUnknown = true)}, which let a
 * caller send anything alongside the grants and be told nothing. The permissions payload the
 * server returns is wider than the one it accepts — a grant comes back carrying the user's name
 * and email, and goes in carrying an identifier alone — so a client that echoed a response back at the
 * server was quietly relying on that tolerance. It is refused now, and the clients send what the
 * request declares.
 */
class PermissionRequestStrictnessTest {

  private static final String WITH_ONLY_WHAT_THE_REQUEST_DECLARES = """
      {"owner": {"@id": "https://metadatacenter.org/users/1"},
       "userPermissions": [{"user": {"@id": "https://metadatacenter.org/users/2"}, "role": "viewer"}],
       "groupPermissions": []}
      """;

  private static final String WITH_A_RESPONSE_ECHOED_BACK = """
      {"owner": {"@id": "https://metadatacenter.org/users/1"},
       "userPermissions": [{"user": {"@id": "https://metadatacenter.org/users/2",
                                     "firstName": "Ada", "lastName": "Lovelace",
                                     "email": "ada@example.org"},
                            "role": "viewer"}],
       "groupPermissions": []}
      """;

  @Test
  void aRequestCarryingOnlyIdentifiersIsRead() throws Exception {
    ResourcePermissionsRequest request = JsonMapper.STRICT_MAPPER.readValue(
        WITH_ONLY_WHAT_THE_REQUEST_DECLARES, ResourcePermissionsRequest.class);

    assertNotNull(request.getOwner());
    assertEquals(1, request.getUserPermissions().size());
  }

  @Test
  void aResponseEchoedBackIsRefused() {
    assertThrows(UnrecognizedPropertyException.class,
        () -> JsonMapper.STRICT_MAPPER.readValue(
            WITH_A_RESPONSE_ECHOED_BACK, ResourcePermissionsRequest.class),
        "a grant carrying the user's name is the response shape, not the request shape");
  }

  @Test
  void anUnknownTopLevelPropertyIsRefused() {
    String withExtra = """
        {"owner": {"@id": "https://metadatacenter.org/users/1"},
         "userPermissions": [], "groupPermissions": [], "somethingElse": true}
        """;

    assertThrows(UnrecognizedPropertyException.class,
        () -> JsonMapper.STRICT_MAPPER.readValue(withExtra, ResourcePermissionsRequest.class));
  }
  private static final String PERMISSIONS = """
      {"owner": {"@id": "https://metadatacenter.org/users/1"%s},
       "userPermissions": [{"user": {"@id": "https://metadatacenter.org/users/2"%s}, "role": "viewer"%s}],
       "groupPermissions": [{"group": {"@id": "https://metadatacenter.org/groups/3"%s}, "role": "viewer"%s}]%s}
      """;

  private static final String GROUP_USER = """
      {"user": {"@id": "https://metadatacenter.org/users/2"%s}, "administrator": false, "member": true%s}
      """;

  /**
   * Every type a permission request is read into, each with the unknown key placed in its own object.
   * The position is the format argument that receives the key; every other position stays empty.
   */
  static Stream<Arguments> everyRequestType() {
    return Stream.of(
        Arguments.of(ResourcePermissionsRequest.class, PERMISSIONS, 5, ResourcePermissionsRequest.class),
        Arguments.of(ResourcePermissionsRequest.class, PERMISSIONS, 0, ResourcePermissionUser.class),
        Arguments.of(ResourcePermissionsRequest.class, PERMISSIONS, 2, ResourcePermissionUserPermissionPair.class),
        Arguments.of(ResourcePermissionsRequest.class, PERMISSIONS, 3, ResourcePermissionGroup.class),
        Arguments.of(ResourcePermissionsRequest.class, PERMISSIONS, 4, ResourcePermissionGroupPermissionPair.class),
        Arguments.of(CategoryPermissionRequest.class, PERMISSIONS, 5, CategoryPermissionRequest.class),
        Arguments.of(CategoryPermissionRequest.class, PERMISSIONS, 1, CategoryPermissionUser.class),
        Arguments.of(CategoryPermissionRequest.class, PERMISSIONS, 2, CategoryPermissionUserPermissionPair.class),
        Arguments.of(CategoryPermissionRequest.class, PERMISSIONS, 3, CategoryPermissionGroup.class),
        Arguments.of(CategoryPermissionRequest.class, PERMISSIONS, 4, CategoryPermissionGroupPermissionPair.class),
        Arguments.of(CedarGroupUserRequest.class, GROUP_USER, 1, CedarGroupUserRequest.class),
        Arguments.of(CedarGroupUserRequest.class, GROUP_USER, 0, ResourcePermissionUser.class));
  }

  @ParameterizedTest(name = "{3} inside {0}")
  @MethodSource("everyRequestType")
  void everyRequestTypeRefusesAnUnknownKeyAtItsOwnLevel(Class<?> requestType, String shape, int position,
                                                        Class<?> refusingType) {
    int positions = shape.split("%s", -1).length - 1;
    Object[] declared = new Object[positions];
    Object[] extended = new Object[positions];
    java.util.Arrays.fill(declared, "");
    java.util.Arrays.fill(extended, "");
    extended[position] = ", \"unexpected\": true";

    assertDoesNotThrow(() -> JsonMapper.STRICT_MAPPER.readValue(String.format(shape, declared), requestType));
    UnrecognizedPropertyException refusal = assertThrows(UnrecognizedPropertyException.class,
        () -> JsonMapper.STRICT_MAPPER.readValue(String.format(shape, extended), requestType));
    assertEquals("unexpected", refusal.getPropertyName());
    assertEquals(refusingType, refusal.getReferringClass());
  }
}
