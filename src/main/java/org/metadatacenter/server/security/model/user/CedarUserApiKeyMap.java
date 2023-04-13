package org.metadatacenter.server.security.model.user;

import org.metadatacenter.util.json.JsonMapper;

import java.io.IOException;
import java.util.HashMap;

public class CedarUserApiKeyMap extends HashMap<String, CedarUserApiKey> {

  public CedarUserApiKeyMap() {
  }

  public CedarUserApiKeyMap(String jsonSource) {
    try {
      CedarUserApiKeyMap deser = JsonMapper.MAPPER.readValue(jsonSource, CedarUserApiKeyMap.class);
      for (String key : deser.keySet()) {
        this.put(key, deser.get(key));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
