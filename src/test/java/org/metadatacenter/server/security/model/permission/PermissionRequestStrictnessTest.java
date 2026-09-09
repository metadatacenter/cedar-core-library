package org.metadatacenter.server.security.model.permission;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.junit.jupiter.api.Test;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionsRequest;
import org.metadatacenter.util.json.JsonMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
