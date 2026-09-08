package org.metadatacenter.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.metadatacenter.error.CedarErrorType;
import org.metadatacenter.http.CedarResponseStatus;
import org.metadatacenter.server.result.BackendCallResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A backend failure reaches the mapper with a decided status either way: the first recorded error's,
 * or 500 when the call failed without recording one.
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
}
