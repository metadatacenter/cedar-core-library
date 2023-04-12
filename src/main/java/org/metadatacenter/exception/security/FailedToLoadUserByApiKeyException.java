package org.metadatacenter.exception.security;

import org.metadatacenter.error.CedarErrorKey;

public class FailedToLoadUserByApiKeyException extends CedarAccessException {

  public FailedToLoadUserByApiKeyException(Exception ex) {
    super("Failed to load user info by apiKey.", CedarErrorKey.USER_INFO_LOAD_BY_API_KEY_FAILED, null, ex);
  }
}
