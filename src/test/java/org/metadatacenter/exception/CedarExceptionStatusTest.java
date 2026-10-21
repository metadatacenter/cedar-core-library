package org.metadatacenter.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.metadatacenter.error.CedarErrorKey;
import org.metadatacenter.error.CedarErrorPack;
import org.metadatacenter.http.CedarResponseStatus;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The exception mapper answers with the status in the exception's pack. Each of these refusals
 * decides that status itself, so a site that throws one cannot report a client's mistake as a server
 * fault by forgetting to choose.
 */
class CedarExceptionStatusTest {

  @Test
  @DisplayName("Each kind of refusal decides the status its kind implies")
  void eachRefusalDecidesItsStatus() {
    assertDecided(CedarResponseStatus.FORBIDDEN, new CedarPermissionException("The user may not read the folder"));
    assertDecided(CedarResponseStatus.NOT_FOUND, new CedarObjectNotFoundException("No such template"));
    assertDecided(CedarResponseStatus.BAD_REQUEST,
        new CedarBadRequestException("The body is not JSON", new IOException("Unexpected character")));
  }

  @Test
  @DisplayName("A bad request built from a pack is a 400 that keeps the pack's explanation")
  void badRequestFromAPackKeepsTheExplanation() {
    CedarBadRequestException e = new CedarBadRequestException(new CedarErrorPack()
        .errorKey(CedarErrorKey.INVALID_DATA)
        .message("The id must be a full template identifier")
        .parameter("id", "templates/x"));

    assertDecided(CedarResponseStatus.BAD_REQUEST, e);
    assertEquals(CedarErrorKey.INVALID_DATA, e.getErrorPack().getErrorKey());
    assertEquals("The id must be a full template identifier", e.getErrorPack().getMessage());
    assertEquals(Map.of("id", "templates/x"), e.getErrorPack().getParameters());
  }

  @Test
  @DisplayName("A bad request built from a cause reports the cause beside its own message")
  void badRequestFromACauseCarriesTheCause() {
    IOException cause = new IOException("Unexpected character");

    CedarBadRequestException e = new CedarBadRequestException("The body is not JSON", cause);

    assertEquals("The body is not JSON : Unexpected character", e.getErrorPack().getMessage());
    assertSame(cause, e.getErrorPack().getOriginalException());
  }

  @Test
  @DisplayName("A missing body field is a 400 that names the field, under the key the caller chose")
  void missingBodyFieldNamesTheField() {
    CedarRequestBodyMissingFieldException plain = new CedarRequestBodyMissingFieldException("schema:name");
    CedarRequestBodyMissingFieldException keyed =
        new CedarRequestBodyMissingFieldException("schema:isBasedOn", CedarErrorKey.MISSING_PARAMETER);

    assertDecided(CedarResponseStatus.BAD_REQUEST, plain);
    assertEquals("The request body must include a non-empty 'schema:name' field", plain.getErrorPack().getMessage());
    assertEquals(CedarErrorKey.INVALID_INPUT, plain.getErrorPack().getErrorKey());
    assertDecided(CedarResponseStatus.BAD_REQUEST, keyed);
    assertEquals(CedarErrorKey.MISSING_PARAMETER, keyed.getErrorPack().getErrorKey());
  }

  private static void assertDecided(CedarResponseStatus expected, CedarException e) {
    assertEquals(expected, e.getErrorPack().getStatus(), e.getClass().getSimpleName());
    assertTrue(e.getErrorPack().hasResolvedStatus(), e.getClass().getSimpleName());
  }
}
