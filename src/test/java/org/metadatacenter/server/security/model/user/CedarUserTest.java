package org.metadatacenter.server.security.model.user;

import org.junit.jupiter.api.Test;
import org.metadatacenter.server.security.model.auth.CedarPermission;
import org.metadatacenter.util.json.JsonMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CedarUserTest {

  /**
   * The graph stores a user's permissions as names on the user record, and every permission check a
   * server makes reads them back through {@code has}. The answer comes from a set rebuilt each time
   * the list is set, so a read that bypassed the setter would deny everything.
   */
  @Test
  void aUserReadFromItsRecordHasExactlyThePermissionsStoredOnIt() throws Exception {
    CedarUser user = JsonMapper.TOLERANT_MAPPER.readValue(
        "{\"@id\":\"https://metadatacenter.org/users/u1\","
            + "\"permissions\":[\"permission_template_create\",\"permission_folder_read\"]}", CedarUser.class);

    assertTrue(user.has(CedarPermission.TEMPLATE_CREATE));
    assertTrue(user.has(CedarPermission.FOLDER_READ));
    assertFalse(user.has(CedarPermission.TEMPLATE_DELETE));
    assertFalse(user.has(null));

    user.setPermissions(List.of(CedarPermission.FOLDER_READ.getPermissionName()));
    assertFalse(user.has(CedarPermission.TEMPLATE_CREATE), "a replaced list withdraws what it no longer holds");
    assertTrue(user.has(CedarPermission.FOLDER_READ));
  }

  /** A server calling another on the user's behalf authenticates with this header. */
  @Test
  void callsOnTheUsersBehalfUseTheFirstEnabledKey() {
    CedarUser user = new CedarUser();
    user.setApiKeys(List.of(key("revoked", false), key("current", true), key("spare", true)));

    assertEquals("current", user.getFirstActiveApiKey());
    assertEquals("apiKey current", user.getFirstApiKeyAuthHeader());
  }

  @Test
  void aUserWithNoEnabledKeyHasNoHeaderToSend() {
    CedarUser revokedOnly = new CedarUser();
    revokedOnly.setApiKeys(List.of(key("revoked", false)));
    CedarUser noKeys = new CedarUser();
    noKeys.setApiKeys(null);

    assertNull(revokedOnly.getFirstApiKeyAuthHeader());
    assertNull(new CedarUser().getFirstApiKeyAuthHeader());
    assertNull(noKeys.getFirstActiveApiKey());
  }

  private static CedarUserApiKey key(String secret, boolean enabled) {
    CedarUserApiKey key = new CedarUserApiKey();
    key.setKey(secret);
    key.setEnabled(enabled);
    return key;
  }
}
