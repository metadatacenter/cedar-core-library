package org.metadatacenter.exception.security;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * The refusal of an Authorization header CEDAR does not recognize reaches the response body and the
 * exception mapper's log. The header carries a credential after its scheme, so the refusal names the
 * scheme and nothing after it.
 */
class UnknownAuthorizationTypeExceptionTest {

  @ParameterizedTest
  @CsvSource({"Basic dGVzdDE6dGVzdDE=,Basic,dGVzdDE6dGVzdDE=", "Digest username=\"u\" response=\"r\",Digest,response"})
  void namesTheSchemeAndNeverTheCredential(String header, String scheme, String credential) {
    UnknownAuthorizationTypeException refusal = new UnknownAuthorizationTypeException(header);

    assertFalse(refusal.getMessage().contains(credential));
    assertFalse(refusal.getErrorPack().getMessage().contains(credential));
    assertFalse(refusal.getErrorPack().getParameters().toString().contains(credential));
    assertEquals(scheme, refusal.getErrorPack().getParameters().get("authScheme"));
  }

  /** Without a scheme the whole header may be a credential, a key pasted without its prefix for one. */
  @ParameterizedTest
  @ValueSource(strings = {"0f5a3b0e6f1a4d1b9c2e1a2b3c4d5e6f", ""})
  void aHeaderWithNoSchemeIsNotRepeated(String header) {
    UnknownAuthorizationTypeException refusal = new UnknownAuthorizationTypeException(header);

    if (!header.isEmpty()) {
      assertFalse(refusal.getMessage().contains(header));
      assertFalse(refusal.getErrorPack().getParameters().toString().contains(header));
    }
    assertNull(refusal.getErrorPack().getParameters().get("authScheme"));
  }
}
