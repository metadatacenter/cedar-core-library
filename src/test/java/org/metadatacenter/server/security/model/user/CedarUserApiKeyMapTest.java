package org.metadatacenter.server.security.model.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.metadatacenter.util.json.JsonMapper;

public class CedarUserApiKeyMapTest {

  private static final String ONE_KEY =
      "{\"secretA\":{\"id\":\"idA\",\"key\":\"secretA\",\"serviceName\":\"svc\",\"enabled\":true}}";

  /** The graph stores the map as a JSON string, which Jackson passes to the String constructor. */
  private static CedarUserApiKeyMap asStoredInTheGraph(String stored) throws Exception {
    return JsonMapper.MAPPER.readValue(JsonMapper.MAPPER.writeValueAsString(stored), CedarUserApiKeyMap.class);
  }

  @Test
  public void aStoredMapIsReadThroughTheStringConstructor() throws Exception {
    CedarUserApiKeyMap map = asStoredInTheGraph(ONE_KEY);

    Assertions.assertEquals(1, map.size());
    Assertions.assertEquals("secretA", map.get("secretA").getKey());
    Assertions.assertFalse(map.isUnreadable());
  }

  @Test
  public void aFieldFromAnotherReleaseDoesNotMakeTheKeysUnreadable() throws Exception {
    String fromALaterRelease =
        "{\"secretA\":{\"id\":\"idA\",\"key\":\"secretA\",\"fieldAddedLater\":42}}";

    CedarUserApiKeyMap map = asStoredInTheGraph(fromALaterRelease);

    Assertions.assertFalse(map.isUnreadable(), "an unknown property must not cost the user their keys");
    Assertions.assertEquals(1, map.size());
    Assertions.assertEquals("secretA", map.get("secretA").getKey());
  }

  @Test
  public void aCorruptMapIsMarkedUnreadableRatherThanEmpty() throws Exception {
    CedarUserApiKeyMap map = asStoredInTheGraph("{\"secretA\":{\"id\":");

    Assertions.assertTrue(map.isUnreadable());
    Assertions.assertTrue(map.isEmpty(), "nothing could be recovered, so the map is also empty");
  }

  @Test
  public void aUserWithNoKeysIsNotMarkedUnreadable() throws Exception {
    CedarUserApiKeyMap map = asStoredInTheGraph("{}");

    Assertions.assertFalse(map.isUnreadable());
    Assertions.assertTrue(map.isEmpty());
  }

  @Test
  public void theUnreadableFlagIsNotSerializedIntoTheStoredMap() throws Exception {
    CedarUserApiKeyMap map = asStoredInTheGraph("{\"secretA\":{\"id\":");

    String written = JsonMapper.MAPPER.writeValueAsString(map);

    Assertions.assertFalse(written.contains("unreadable"), "the flag is state about the read, not a stored key");
  }
}
