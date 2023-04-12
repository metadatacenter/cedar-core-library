package org.metadatacenter.server.security.model.user;

import org.metadatacenter.id.CedarUserId;

public interface CedarUserRepresentation {

  String getId();

  CedarUserId getResourceId();

  String getFirstName();

  String getLastName();

  String getEmail();

}
