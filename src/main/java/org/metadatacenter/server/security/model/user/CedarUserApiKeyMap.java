package org.metadatacenter.server.security.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.metadatacenter.util.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;

/**
 * The stored API keys, keyed by secret. The graph holds this as a JSON string, which Jackson hands
 * to the single-argument constructor below when it reads a user record.
 */
public class CedarUserApiKeyMap extends HashMap<String, CedarUserApiKey> {

  private static final Logger log = LoggerFactory.getLogger(CedarUserApiKeyMap.class);

  private boolean unreadable;

  public CedarUserApiKeyMap() {
  }

  public CedarUserApiKeyMap(String jsonSource) {
    try {
      CedarUserApiKeyMap deser = JsonMapper.MAPPER.readValue(jsonSource, CedarUserApiKeyMap.class);
      for (String key : deser.keySet()) {
        this.put(key, deser.get(key));
      }
    } catch (IOException e) {
      // A map that could not be read counts zero entries, exactly as a user with no keys does, but
      // the two mean opposite things: the stored secrets still authenticate. Anything that writes
      // the key set back must consult isUnreadable() and refuse, or it persists the loss.
      unreadable = true;
      log.error("Could not deserialize the stored API key map; the keys it holds are unreadable, not absent", e);
    }
  }

  /**
   * Whether the stored map failed to parse. An unreadable map is empty for want of anything to put
   * in it, so emptiness alone cannot be read as "this user has no keys".
   */
  @JsonIgnore
  public boolean isUnreadable() {
    return unreadable;
  }
}
