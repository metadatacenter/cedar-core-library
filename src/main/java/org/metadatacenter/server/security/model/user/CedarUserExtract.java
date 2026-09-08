package org.metadatacenter.server.security.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.metadatacenter.id.CedarUserId;

@Schema(name = "CedarUserReference",
    description = "A reference to a user. It appears wherever a user is named beside something "
        + "else, as the owner of a resource, the holder of a grant, or a member of a group. The "
        + "name and email accompany the identifier so a caller need not fetch the user record to "
        + "display them.")
public class CedarUserExtract implements CedarUserRepresentation {

  private String id;
  private String firstName;
  private String lastName;
  private String email;

  public CedarUserExtract() {
  }

  public CedarUserExtract(String id, String firstName, String lastName, String email) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
  }

  @Override
  @JsonProperty("@id")
  @Schema(name = "@id", requiredMode = Schema.RequiredMode.REQUIRED, format = "uri",
      description = "Identifier of the user.")
  public String getId() {
    return id;
  }

  @Override
  @JsonIgnore
  public CedarUserId getResourceId() {
    return CedarUserId.build(id);
  }

  @JsonProperty("@id")
  public void setId(String id) {
    this.id = id;
  }

  @Override
  @Schema(name = "firstName", description = "Given name of the user.")
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Override
  @Schema(name = "lastName", description = "Family name of the user.")
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Override
  @Schema(name = "email", format = "email", description = "Email address of the user.")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
