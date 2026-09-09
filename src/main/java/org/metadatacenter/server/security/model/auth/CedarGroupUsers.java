package org.metadatacenter.server.security.model.auth;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

@Schema(name = "GroupMembership",
    description = "Everyone a group names, its members and its administrators together. A "
        + "membership is always given in full rather than a page at a time, and a write replaces "
        + "it rather than adding to it.",
    additionalProperties = Schema.AdditionalPropertiesValue.FALSE)
public class CedarGroupUsers {

  private final List<CedarGroupUser> users;


  public CedarGroupUsers() {
    users = new ArrayList<>();
  }

  @Schema(name = "users", requiredMode = Schema.RequiredMode.REQUIRED,
      description = "One entry per user the group names.")
  public List<CedarGroupUser> getUsers() {
    return users;
  }

  public void addUser(CedarGroupUser user) {
    users.add(user);
  }

}
