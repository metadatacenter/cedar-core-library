package org.metadatacenter.exception.security;

import org.metadatacenter.error.CedarErrorKey;
import org.metadatacenter.error.CedarSuggestedAction;

import javax.ws.rs.core.Response;

public class AuthorizationNotFoundException extends CedarAccessException {

  public AuthorizationNotFoundException() {
    super("Authorization not found.", CedarErrorKey.AUTHORIZATION_NOT_FOUND, CedarSuggestedAction
        .PROVIDE_AUTHORIZATION_HEADER);
    errorPack.status(Response.Status.UNAUTHORIZED);
  }
}
