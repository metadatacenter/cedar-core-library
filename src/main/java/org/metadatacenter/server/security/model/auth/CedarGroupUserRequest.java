package org.metadatacenter.server.security.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionUser;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(name = "GroupMemberRequest",
    description = "One user's place in a group, as a write states it. The user is named by "
        + "identifier alone. The two flags say what that user's standing should become.",
    additionalProperties = Schema.AdditionalPropertiesValue.TRUE)
public class CedarGroupUserRequest {

  private ResourcePermissionUser user;
  private boolean administrator;
  private boolean member;

  public CedarGroupUserRequest() {
  }

  public CedarGroupUserRequest(ResourcePermissionUser user, boolean administrator, boolean member) {
    this.user = user;
    this.administrator = administrator;
    this.member = member;
  }

  @Schema(name = "user", requiredMode = Schema.RequiredMode.REQUIRED,
      description = "The user this entry is about.")
  public ResourcePermissionUser getUser() {
    return user;
  }

  public void setUser(ResourcePermissionUser user) {
    this.user = user;
  }

  @Schema(name = "administrator", requiredMode = Schema.RequiredMode.REQUIRED,
      description = "Whether the user may change who belongs to the group.")
  public boolean isAdministrator() {
    return administrator;
  }

  public void setAdministrator(boolean administrator) {
    this.administrator = administrator;
  }

  @Schema(name = "member", requiredMode = Schema.RequiredMode.REQUIRED,
      description = "Whether the user belongs to the group.")
  public boolean isMember() {
    return member;
  }

  public void setMember(boolean member) {
    this.member = member;
  }
}
