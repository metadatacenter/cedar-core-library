package org.metadatacenter.exception.security;

import org.metadatacenter.error.CedarErrorKey;
import org.metadatacenter.error.CedarSuggestedAction;
import org.metadatacenter.http.CedarResponseStatus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnknownAuthorizationTypeException extends CedarAccessException {

  // An auth-scheme is an RFC 9110 token, and whitespace separates it from the credentials. A header
  // without whitespace names no scheme and may be a bare credential.
  private static final Pattern SCHEME = Pattern.compile("^([!#$%&'*+.^_`|~0-9A-Za-z-]+)\\s");

  // The header carries a credential, so the message and the parameters name the scheme at most.
  public UnknownAuthorizationTypeException(String authHeader) {
    super(schemeOf(authHeader) == null ? "Found unknown Authorization data."
            : "Found unknown Authorization data with the scheme '" + schemeOf(authHeader) + "'.",
        CedarErrorKey.AUTHORIZATION_TYPE_UNKNOWN, null, null);
    String scheme = schemeOf(authHeader);
    if (scheme != null) {
      errorPack.parameter("authScheme", scheme);
    }
    errorPack.status(CedarResponseStatus.UNAUTHORIZED);
    errorPack.suggestedAction(CedarSuggestedAction.LOGOUT_IMMEDIATELY);
  }

  private static String schemeOf(String authHeader) {
    if (authHeader == null) {
      return null;
    }
    Matcher matcher = SCHEME.matcher(authHeader);
    return matcher.find() ? matcher.group(1) : null;
  }
}
