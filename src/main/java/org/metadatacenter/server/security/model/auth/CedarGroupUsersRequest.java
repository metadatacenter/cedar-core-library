package org.metadatacenter.server.security.model.auth;

import java.util.ArrayList;
import java.util.List;

public class CedarGroupUsersRequest {

  private List<CedarGroupUserRequest> users;

  public CedarGroupUsersRequest() {
    users = new ArrayList<>();
  }

  public List<CedarGroupUserRequest> getUsers() {
    return users;
  }

  public void setUsers(List<CedarGroupUserRequest> users) {
    this.users = users;
  }
}
