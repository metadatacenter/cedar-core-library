package org.metadatacenter.exception.security;

import org.metadatacenter.error.CedarErrorKey;
import org.metadatacenter.error.CedarSuggestedAction;
import org.metadatacenter.http.CedarResponseStatus;

public class UnknownAuthorizationTypeException extends CedarAccessException {

  public UnknownAuthorizationTypeException(String authHeader) {
    super("Found unknown Authorization data: '" + authHeader + "'.",
        CedarErrorKey.AUTHORIZATION_TYPE_UNKNOWN, null, null);
    errorPack.parameter("authHeader", authHeader);
    errorPack.status(CedarResponseStatus.UNAUTHORIZED);
    errorPack.suggestedAction(CedarSuggestedAction.LOGOUT_IMMEDIATELY);
  }
}
