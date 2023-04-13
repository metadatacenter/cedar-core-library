package org.metadatacenter.exception.security;

import org.metadatacenter.error.CedarErrorKey;

public class ApiKeyNotFoundException extends CedarAccessException {

  public ApiKeyNotFoundException(String apiKey) {
    super("The apiKey was not found: '" + apiKey + "'.", CedarErrorKey.API_KEY_NOT_FOUND, null);
    errorPack.parameter("apiKey", apiKey);
  }
}
