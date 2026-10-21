package org.metadatacenter.error;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.metadatacenter.http.CedarResponseStatus;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The keys a client can rely on finding in an exception-mapped failure.
 *
 * <p>CEDAR renders errors two ways: this pack, which the exception mapper serializes, and the map
 * {@code CedarResponse} builds. They named the same things differently — {@code message} against
 * {@code errorMessage} — and only one carried a numeric status, so a client reading one set of keys got
 * nulls from half the failures in the system. Both key sets are present here now.
 */
class CedarErrorPackSerializationTest {

  private static JsonNode serialize(CedarErrorPack pack) throws Exception {
    return new ObjectMapper().readTree(new ObjectMapper().writeValueAsString(pack));
  }

  @Test
  @DisplayName("The numeric status is present alongside the enum name")
  void carriesTheNumericStatus() throws Exception {
    JsonNode rendered = serialize(new CedarErrorPack().status(CedarResponseStatus.NOT_FOUND));
    assertEquals(404, rendered.get("statusCode").asInt());
    assertEquals("NOT_FOUND", rendered.get("status").asText(),
        "the enum name stays: removing it would break a client reading it today");
  }

  @Test
  @DisplayName("The message is readable under its one key")
  void carriesTheMessage() throws Exception {
    JsonNode rendered = serialize(new CedarErrorPack().message("the artifact was not found"));
    assertEquals("the artifact was not found", rendered.get("message").asText());
    assertNull(rendered.get("errorMessage"), "the alias is gone from every shape");
  }

  @Test
  @DisplayName("A pack with no explicit status still reports a number")
  void defaultsToAServerError() throws Exception {
    JsonNode rendered = serialize(new CedarErrorPack());
    assertTrue(rendered.get("statusCode").asInt() >= 400,
        "an error pack should never report a success code");
  }

  /**
   * Serialized as it is, a pack carries its source exception whole, with stack frames and the messages
   * of its causes. The exception mapper therefore sends the client a copy with the source exception
   * reset, and logs the original, which must still hold the exception and its causes.
   */
  @Test
  @DisplayName("A reset copy renders no exception, and the original keeps it for the log")
  void resetCopyRendersNoException() throws Exception {
    IOException cause = new IOException("jdbc:mysql://db.internal:3306 refused the connection");
    CedarErrorPack original = new CedarErrorPack().status(CedarResponseStatus.NOT_FOUND).message("No such folder")
        .sourceException(new IllegalStateException("lookup failed", cause));

    CedarErrorPack clientCopy = new CedarErrorPack(original);
    clientCopy.resetSourceException();

    String rendered = new ObjectMapper().writeValueAsString(clientCopy);
    assertFalse(rendered.contains("db.internal"), rendered);
    assertFalse(rendered.contains("stackTrace"), rendered);
    assertEquals("No such folder", serialize(clientCopy).get("message").asText());
    assertEquals(404, serialize(clientCopy).get("statusCode").asInt());

    assertEquals("lookup failed", original.getOriginalException().getMessage());
    assertEquals("lookup failed", original.getSourceException().getMessage());
    assertEquals("jdbc:mysql://db.internal:3306 refused the connection",
        original.getSourceException().getCauses().get(0).getMessage());
  }
}
