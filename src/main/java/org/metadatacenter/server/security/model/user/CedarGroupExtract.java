package org.metadatacenter.server.security.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CedarGroupReference",
    description = "A reference to a group. It appears wherever a group is named beside something "
        + "else, as the holder of a grant or as a group a user belongs to. The name accompanies "
        + "the identifier so a caller need not fetch the group record to display it.")
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