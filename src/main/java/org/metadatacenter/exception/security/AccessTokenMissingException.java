package org.metadatacenter.exception.security;

import org.metadatacenter.error.CedarErrorKey;
import org.metadatacenter.error.CedarSuggestedAction;

public class AccessTokenMissingException extends CedarAccessException {

  public AccessTokenMissingException() {
    super("Access token is missing.", CedarErrorKey.TOKEN_MISSING, CedarSuggestedAction.LOGOUT);
  }
}
