package org.metadatacenter.server.security.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CedarGroupReference",
    description = "A group as CEDAR names one beside something else: the holder of a grant, or a "
        + "group a user belongs to. The identifier is the reference; the name is carried so a "
        + "caller need not resolve it to display the group.")
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
  @Schema(name = "@id", requiredMode = Schema.RequiredMode.REQUIRED, format = "uri",
      description = "Identifier of the group.")
  public String getId() {
    return id;
  }

  @JsonProperty("@id")
  public void setId(String id) {
    this.id = id;
  }

  @JsonProperty("schema:name")
  @Schema(name = "schema:name", description = "Display name of the group.")
  public String getName() {
    return name;
  }

  @JsonProperty("schema:name")
  public void setName(String name) {
    this.name = name;
  }
}