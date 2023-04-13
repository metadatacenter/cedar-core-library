package org.metadatacenter.exception.security;

import org.metadatacenter.error.CedarErrorKey;

public class FailedToLoadUserByTokenException extends CedarAccessException {

  public FailedToLoadUserByTokenException(Exception ex) {
    super("Failed to load user info by token.", CedarErrorKey.USER_INFO_LOAD_BY_TOKEN_FAILED, null, ex);
  }
}
