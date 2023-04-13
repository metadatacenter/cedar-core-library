package org.metadatacenter.server.security.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionUser;

@JsonIgnoreProperties(ignoreUnknown = true)
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

  public ResourcePermissionUser getUser() {
    return user;
  }

  public void setUser(ResourcePermissionUser user) {
    this.user = user;
  }

  public boolean isAdministrator() {
    return administrator;
  }

  public void setAdministrator(boolean administrator) {
    this.administrator = administrator;
  }

  public boolean isMember() {
    return member;
  }

  public void setMember(boolean member) {
    this.member = member;
  }
}
