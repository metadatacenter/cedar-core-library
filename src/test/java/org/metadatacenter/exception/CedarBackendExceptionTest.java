package org.metadatacenter.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.metadatacenter.error.CedarAssertionResult;
import org.metadatacenter.error.CedarErrorKey;
import org.metadatacenter.error.CedarErrorPack;
import org.metadatacenter.error.CedarErrorType;
import org.metadatacenter.http.CedarResponseStatus;
import org.metadatacenter.server.result.BackendCallResult;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A backend failure reaches the mapper with a decided status: the first recorded error's, 500 when the
 * call failed without recording one, or the status a failed assertion chose.
 */
class CedarBackendExceptionTest {

  @Test
  @DisplayName("The first recorded error decides the status")
  void firstErrorDecides() {
    BackendCallResult<Void> result = new BackendCallResult<>();
    result.addError(CedarErrorType.NOT_FOUND).message("no such folder");
    CedarBackendException e = new CedarBackendException(result);
    assertEquals(CedarResponseStatus.NOT_FOUND, e.getErrorPack().getStatus());
    assertTrue(e.getErrorPack().hasResolvedStatus());
  }

  @Test
  @DisplayName("A failure with no recorded error is a decided 500")
  void unrecordedFailureIsADecidedInternalServerError() {
    CedarBackendException e = new CedarBackendException(new BackendCallResult<Void>());
    assertEquals(CedarResponseStatus.INTERNAL_SERVER_ERROR, e.getErrorPack().getStatus());
    assertTrue(e.getErrorPack().hasResolvedStatus());
  }

  /**
   * The group server refuses a duplicate group name this way. The client recognizes the collision by
   * its key, not by the message.
   */
  @Test
  @DisplayName("A failed assertion keeps its status, key, message and parameters")
  void failedAssertionKeepsWhatItDecided() {
    CedarAssertionResult assertion = new CedarAssertionResult("There is a group with the new name")
        .parameter("schema:name", "Lab")
        .errorKey(CedarErrorKey.GROUP_ALREADY_PRESENT)
        .conflict();

    CedarBackendException e = new CedarBackendException(assertion);

    CedarErrorPack pack = e.getErrorPack();
    assertEquals(CedarResponseStatus.CONFLICT, pack.getStatus());
    assertTrue(pack.hasResolvedStatus());
    assertEquals(CedarErrorKey.GROUP_ALREADY_PRESENT, pack.getErrorKey());
    assertEquals("There is a group with the new name", pack.getMessage());
    assertEquals("There is a group with the new name", e.getMessage());
    assertEquals(Map.of("schema:name", "Lab"), pack.getParameters());
  }
}
