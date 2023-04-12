package org.metadatacenter.exception.security;

import org.metadatacenter.error.CedarErrorKey;
import org.metadatacenter.error.CedarSuggestedAction;

public class AccessTokenExpiredException extends CedarAccessException {

  private final int expiration;

  public AccessTokenExpiredException(int expiration) {
    super("Access token expired. Please refresh it.", CedarErrorKey.TOKEN_EXPIRED, CedarSuggestedAction.REFRESH_TOKEN);
    this.expiration = expiration;
  }

  public int getExpiration() {
    return expiration;
  }
}
