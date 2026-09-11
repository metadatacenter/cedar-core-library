package org.metadatacenter.error;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.metadatacenter.http.CedarResponseStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
