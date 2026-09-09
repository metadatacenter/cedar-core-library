package org.metadatacenter.server.security.model.auth;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

@Schema(name = "GroupMembershipRequest",
    description = "A replacement membership for a group. Each entry names a user by identifier "
        + "alone, because the name and email a listing carries are read from the user record "
        + "rather than set here.",
    additionalProperties = Schema.AdditionalPropertiesValue.FALSE)
public class CedarGroupUsersRequest {

  private List<CedarGroupUserRequest> users;

  public CedarGroupUsersRequest() {
    users = new ArrayList<>();
  }

  @Schema(name = "users", requiredMode = Schema.RequiredMode.REQUIRED,
      description = "One entry per user the group should name once the write has been applied.")
  public List<CedarGroupUserRequest> getUsers() {
    return users;
  }

  public void setUsers(List<CedarGroupUserRequest> users) {
    this.users = users;
  }
}
