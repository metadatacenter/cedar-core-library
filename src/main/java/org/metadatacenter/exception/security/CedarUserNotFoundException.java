package org.metadatacenter.exception.security;

import org.metadatacenter.error.CedarErrorKey;
import org.metadatacenter.error.CedarSuggestedAction;
import org.metadatacenter.http.CedarResponseStatus;

public class CedarUserNotFoundException extends CedarAccessException {

  public CedarUserNotFoundException(Exception ex) {
    super("CEDAR user not found.", CedarErrorKey.CEDAR_USER_NOT_FOUND, CedarSuggestedAction.LOGOUT, ex);
    errorPack.status(CedarResponseStatus.UNAUTHORIZED);
  }
}
