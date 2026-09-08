package org.metadatacenter.server.security.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import org.metadatacenter.server.security.model.user.CedarUserExtract;
import org.metadatacenter.id.CedarUserId;

@JsonIgnoreProperties(value = {"userId"})
@Schema(name = "GroupMember",
    description = "One user's place in a group. A member belongs to the group. An administrator "
        + "may change who belongs to it. The two are recorded independently, so a user may "
        + "administer a group without belonging to it.",
    additionalProperties = Schema.AdditionalPropertiesValue.TRUE)
public class CedarGroupUser {

  private CedarUserExtract user;
  private boolean administrator;
  private boolean member;

  public CedarGroupUser() {
  }

  public CedarGroupUser(CedarUserExtract user, boolean administrator, boolean member) {
    this.user = user;
    this.administrator = administrator;
    this.member = member;
  }

  @Schema(name = "user", requiredMode = Schema.RequiredMode.REQUIRED,
      description = "The user this entry is about.")
  public CedarUserExtract getUser() {
    return user;
  }

  @JsonIgnore
  public CedarUserId getResourceId() {
    return CedarUserId.build(user.getId());
  }

  public void setUser(CedarUserExtract user) {
    this.user = user;
  }

  @Schema(name = "administrator", requiredMode = Schema.RequiredMode.REQUIRED,
      description = "Whether the user may change who belongs to the group.")
  public boolean isAdministrator() {
    return administrator;
  }

  @Schema(name = "member", requiredMode = Schema.RequiredMode.REQUIRED,
      description = "Whether the user belongs to the group.")
  public boolean isMember() {
    return member;
  }
}
