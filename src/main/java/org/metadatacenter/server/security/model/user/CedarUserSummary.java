package org.metadatacenter.server.security.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CedarUserSummary {

  private String id;
  private String screenName;

  public CedarUserSummary() {
  }

  @JsonProperty("@id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getScreenName() {
    return screenName;
  }

  public void setScreenName(String screenName) {
    this.screenName = screenName;
  }

}
