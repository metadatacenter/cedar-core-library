package org.metadatacenter.server.security.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.id.CedarUserId;

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
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Override
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Override
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
