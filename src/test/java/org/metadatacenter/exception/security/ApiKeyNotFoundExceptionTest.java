package org.metadatacenter.exception.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ApiKeyNotFoundExceptionTest {

  @Test
  void doesNotRetainTheRejectedCredential() {
    String rejectedApiKey = "secret-api-key-value";

    ApiKeyNotFoundException exception = new ApiKeyNotFoundException(rejectedApiKey);

    assertEquals("The apiKey was not found.", exception.getMessage());
    assertFalse(exception.getErrorPack().getMessage().contains(rejectedApiKey));
    assertFalse(exception.getErrorPack().getParameters().containsValue(rejectedApiKey));
  }
}
