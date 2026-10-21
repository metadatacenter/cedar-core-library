package org.metadatacenter.exception.security;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.metadatacenter.error.CedarErrorKey;
import org.metadatacenter.error.CedarSuggestedAction;
import org.metadatacenter.http.CedarResponseStatus;

import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The refusals for a caller CEDAR cannot identify, and for one that lacks a permission it must own. A
 * client acts on the status, the error key and the suggested action. It refreshes an expired token,
 * logs out on an invalid one, and treats a 403 as a missing permission rather than as a reason to
 * authenticate again.
 */
class CedarAccessExceptionTest {

  @ParameterizedTest(name = "{0}")
  @MethodSource("refusals")
  void eachRefusalCarriesTheStatusAndKeyAClientActsOn(String name, CedarAccessException refusal,
                                                     CedarResponseStatus status, CedarErrorKey errorKey) {
    assertEquals(status, refusal.getErrorPack().getStatus());
    assertTrue(refusal.getErrorPack().hasResolvedStatus());
    assertEquals(errorKey, refusal.getErrorPack().getErrorKey());
  }

  private static Stream<Arguments> refusals() {
    IOException cause = new IOException("the user service did not answer");
    return Stream.of(
        Arguments.of("token expired", new AccessTokenExpiredException(300),
            CedarResponseStatus.UNAUTHORIZED, CedarErrorKey.TOKEN_EXPIRED),
        Arguments.of("token missing", new AccessTokenMissingException(),
            CedarResponseStatus.UNAUTHORIZED, CedarErrorKey.TOKEN_MISSING),
        Arguments.of("token invalid", new InvalidOfflineAccessTokenException(cause),
            CedarResponseStatus.UNAUTHORIZED, CedarErrorKey.TOKEN_INVALID),
        Arguments.of("no authorization", new AuthorizationNotFoundException(),
            CedarResponseStatus.UNAUTHORIZED, CedarErrorKey.AUTHORIZATION_NOT_FOUND),
        Arguments.of("unknown authorization", new UnknownAuthorizationTypeException("Digest x"),
            CedarResponseStatus.UNAUTHORIZED, CedarErrorKey.AUTHORIZATION_TYPE_UNKNOWN),
        Arguments.of("api key not found", new ApiKeyNotFoundException("secret"),
            CedarResponseStatus.UNAUTHORIZED, CedarErrorKey.API_KEY_NOT_FOUND),
        Arguments.of("user not found", new CedarUserNotFoundException(cause),
            CedarResponseStatus.UNAUTHORIZED, CedarErrorKey.CEDAR_USER_NOT_FOUND),
        Arguments.of("user not loaded by token", new FailedToLoadUserByTokenException(cause),
            CedarResponseStatus.UNAUTHORIZED, CedarErrorKey.USER_INFO_LOAD_BY_TOKEN_FAILED),
        Arguments.of("user not loaded by api key", new FailedToLoadUserByApiKeyException(cause),
            CedarResponseStatus.UNAUTHORIZED, CedarErrorKey.USER_INFO_LOAD_BY_API_KEY_FAILED),
        Arguments.of("permission not owned", new PermissionNotOwnedException("permission_template_create"),
            CedarResponseStatus.FORBIDDEN, CedarErrorKey.PERMISSION_NOT_OWNED));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("directedRefusals")
  void aRefusalThatDirectsTheClientSaysWhatToDo(String name, CedarAccessException refusal,
                                               CedarSuggestedAction action) {
    assertEquals(action, refusal.getErrorPack().getSuggestedAction());
  }

  private static Stream<Arguments> directedRefusals() {
    return Stream.of(
        Arguments.of("token expired", new AccessTokenExpiredException(300), CedarSuggestedAction.REFRESH_TOKEN),
        Arguments.of("token missing", new AccessTokenMissingException(), CedarSuggestedAction.LOGOUT),
        Arguments.of("token invalid", new InvalidOfflineAccessTokenException(), CedarSuggestedAction.LOGOUT),
        Arguments.of("no authorization", new AuthorizationNotFoundException(),
            CedarSuggestedAction.PROVIDE_AUTHORIZATION_HEADER),
        Arguments.of("unknown authorization", new UnknownAuthorizationTypeException("Digest x"),
            CedarSuggestedAction.LOGOUT_IMMEDIATELY),
        Arguments.of("user not found", new CedarUserNotFoundException(null), CedarSuggestedAction.LOGOUT));
  }
}
