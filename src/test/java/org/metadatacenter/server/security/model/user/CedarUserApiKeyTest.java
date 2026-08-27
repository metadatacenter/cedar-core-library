package org.metadatacenter.server.security.model.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.metadatacenter.util.json.JsonMapper;

import java.util.UUID;

public class CedarUserApiKeyTest {

  @Test
  public void legacyKeyGetsAStableNonSecretId() throws Exception {
    CedarUserApiKey key = new CedarUserApiKey();
    key.setKey("0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef");

    String id = key.getId();
    Assertions.assertTrue(id.startsWith("legacy-"));
    Assertions.assertFalse(id.contains(key.getKey()));

    CedarUserApiKey roundTripped = JsonMapper.MAPPER.readValue(
        JsonMapper.MAPPER.writeValueAsString(key), CedarUserApiKey.class);
    Assertions.assertEquals(id, roundTripped.getId());
  }

  @Test
  public void rotatingAKeyDoesNotChangeItsId() {
    CedarUserApiKey key = new CedarUserApiKey();
    key.setKey("old-secret");
    String id = key.getId();

    key.setKey("new-secret");

    Assertions.assertEquals(id, key.getId());
  }

  @Test
  public void explicitIdIsPreserved() {
    CedarUserApiKey key = new CedarUserApiKey();
    String id = UUID.randomUUID().toString();
    key.setId(id);
    key.setKey("secret");

    Assertions.assertEquals(id, key.getId());
  }
}
