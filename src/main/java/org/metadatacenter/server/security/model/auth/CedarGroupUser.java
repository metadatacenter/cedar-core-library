package org.metadatacenter.server.security.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.server.security.model.user.CedarUserExtract;
import org.metadatacenter.id.CedarUserId;

@JsonIgnoreProperties(value = {"userId"})
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

  public CedarUserExtract getUser() {
    return user;
  }

  public CedarUserId getResourceId() {
    return CedarUserId.build(user.getId());
  }

  public void setUser(CedarUserExtract user) {
    this.user = user;
  }

  public boolean isAdministrator() {
    return administrator;
  }

  public boolean isMember() {
    return member;
  }
}
