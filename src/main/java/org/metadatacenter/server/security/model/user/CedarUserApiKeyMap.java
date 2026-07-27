package org.metadatacenter.server.security.model.user;

import org.metadatacenter.util.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;

public class CedarUserApiKeyMap extends HashMap<String, CedarUserApiKey> {

  private static final Logger log = LoggerFactory.getLogger(CedarUserApiKeyMap.class);

  public CedarUserApiKeyMap() {
  }

  public CedarUserApiKeyMap(String jsonSource) {
    try {
      CedarUserApiKeyMap deser = JsonMapper.MAPPER.readValue(jsonSource, CedarUserApiKeyMap.class);
      for (String key : deser.keySet()) {
        this.put(key, deser.get(key));
      }
    } catch (IOException e) {
      // The map is left empty, which reads as "this user has no API keys"; log it so that is not
      // mistaken for the user genuinely having none.
      log.error("Could not deserialize the stored API key map; treating the user as having no keys", e);
    }
  }
}
