package org.metadatacenter.server.security.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CedarGroupExtract {

  private String id;
  private String name;

  // Present so Jackson can deserialize this type, which it could not before: without a no-argument
  // constructor it has no way to instantiate one, and the two-argument constructor is not annotated
  // for property binding. The consequence reached further than the class itself — a permissions
  // response is a CedarNodePermissionsWithExtract, so once an ACL contained a group grant the whole
  // response became unreadable to any Java client using these model classes, while an ACL with only
  // user grants read back fine because CedarUserExtract already had one. The fields are mutable and
  // have setters, so this plus the existing setters is all Jackson needs.
  public CedarGroupExtract() {
  }

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