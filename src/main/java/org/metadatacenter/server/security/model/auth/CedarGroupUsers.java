package org.metadatacenter.server.security.model.auth;

import java.util.ArrayList;
import java.util.List;

public class CedarGroupUsers {

  private final List<CedarGroupUser> users;


  public CedarGroupUsers() {
    users = new ArrayList<>();
  }

  public List<CedarGroupUser> getUsers() {
    return users;
  }

  public void addUser(CedarGroupUser user) {
    users.add(user);
  }

}
