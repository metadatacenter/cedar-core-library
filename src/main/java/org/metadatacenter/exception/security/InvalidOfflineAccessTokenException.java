package org.metadatacenter.exception.security;

import org.metadatacenter.error.CedarErrorKey;
import org.metadatacenter.error.CedarSuggestedAction;

public class InvalidOfflineAccessTokenException extends CedarAccessException {

  public InvalidOfflineAccessTokenException() {
    super("Invalid offline access token.", CedarErrorKey.TOKEN_INVALID, CedarSuggestedAction.LOGOUT);
  }
}
