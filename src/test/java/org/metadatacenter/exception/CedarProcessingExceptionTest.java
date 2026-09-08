package org.metadatacenter.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.metadatacenter.http.CedarResponseStatus;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A processing failure is a server fault, and the exception says so itself rather than leaving the
 * error pack to fall through to its default. The difference matters to the mapper, which flags a
 * status nobody decided.
 */
class CedarProcessingExceptionTest {

  @Test
  @DisplayName("Every constructor decides on 500")
  void everyConstructorDecidesOnInternalServerError() {
    for (CedarException e : new CedarException[]{
        new CedarProcessingException("the store answered badly"),
        new CedarProcessingException(new IOException("read failed")),
        new CedarProcessingException("the store answered badly", new IOException("read failed"))}) {
      assertEquals(CedarResponseStatus.INTERNAL_SERVER_ERROR, e.getErrorPack().getStatus());
      assertTrue(e.getErrorPack().hasResolvedStatus(), "the status must be decided, not defaulted");
    }
  }

  @Test
  @DisplayName("A site that knows better can still chain another status")
  void chainedStatusWins() {
    CedarException e = new CedarProcessingException("the body was not what it claimed").badRequest();
    assertEquals(CedarResponseStatus.BAD_REQUEST, e.getErrorPack().getStatus());
  }
}
