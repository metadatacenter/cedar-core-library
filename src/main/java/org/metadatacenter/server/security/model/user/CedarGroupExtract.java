package org.metadatacenter.server.security.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CedarGroupExtract {

  private String id;
  private String name;

  public CedarGroupExtract(String id, String name) {
    this.id = id;
    this.name = name;
  }

  @JsonProperty("@id")
  public String getId() {
    return id;
  }

  @JsonProperty("@id")
  public void setId(String id) {
    this.id = id;
  }

  @JsonProperty("schema:name")
  public String getName() {
    return name;
  }

  @JsonProperty("schema:name")
  public void setName(String name) {
    this.name = name;
  }
}