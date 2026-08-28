package org.metadatacenter.exception;

import org.junit.jupiter.api.Test;
import org.metadatacenter.http.CedarResponseStatus;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class CedarDependencyUnavailableExceptionTest {

  @Test
  void carriesServiceUnavailableStatusAndTheTransportFailure() {
    IOException cause = new IOException("connection refused");

    CedarDependencyUnavailableException exception =
        new CedarDependencyUnavailableException("User service is unavailable", cause);

    assertEquals(CedarResponseStatus.SERVICE_UNAVAILABLE, exception.getErrorPack().getStatus());
    assertEquals("User service is unavailable", exception.getErrorPack().getMessage());
    assertSame(cause, exception.getErrorPack().getOriginalException());
  }
}
