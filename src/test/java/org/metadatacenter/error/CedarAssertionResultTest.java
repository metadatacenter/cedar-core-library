package org.metadatacenter.error;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.metadatacenter.http.CedarResponseStatus;
import org.metadatacenter.operation.CedarOperationDescriptor;
import org.metadatacenter.operation.CedarOperations;
import org.metadatacenter.server.result.BackendCallResult;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A failed assertion becomes the response through this result's pack. The REST library merges the
 * caller's pack into it when an {@code otherwise...} clause supplies one, and builds it from a backend
 * call that reported an error.
 */
class CedarAssertionResultTest {

  @Test
  @DisplayName("A failed backend call reports its first error")
  void backendCallReportsItsFirstError() {
    BackendCallResult<Void> backendCall = new BackendCallResult<>();
    backendCall.addError(CedarErrorType.NOT_FOUND).message("no such group").parameter("id", "group-1");
    backendCall.addError(CedarErrorType.SERVER_ERROR).message("a later error");

    CedarErrorPack pack = new CedarAssertionResult(backendCall).getErrorPack();

    assertEquals(CedarResponseStatus.NOT_FOUND, pack.getStatus());
    assertEquals(CedarErrorType.NOT_FOUND, pack.getErrorType());
    assertEquals("no such group", pack.getMessage());
    assertEquals(Map.of("id", "group-1"), pack.getParameters());
    assertTrue(pack.hasResolvedStatus());
  }

  @Test
  @DisplayName("Merging a caller's pack that carries only a status keeps the assertion's explanation")
  void mergeKeepsTheAssertionsExplanation() {
    CedarAssertionResult assertion = new CedarAssertionResult("The id is invalid:x")
        .parameter("name", "id")
        .badRequest();
    CedarOperationDescriptor operation = CedarOperations.lookup(Object.class, "id", "x");

    assertion.mergeErrorPack(new CedarErrorPack().status(CedarResponseStatus.NOT_FOUND).operation(operation));

    CedarErrorPack pack = assertion.getErrorPack();
    assertEquals(CedarResponseStatus.NOT_FOUND, pack.getStatus());
    assertTrue(pack.hasResolvedStatus());
    assertEquals("The id is invalid:x", pack.getMessage());
    assertEquals(Map.of("name", "id"), pack.getParameters());
    assertSame(operation, pack.getOperation());
  }

  @Test
  @DisplayName("Merging a caller's pack that explains the failure replaces the assertion's explanation")
  void mergeTakesTheCallersExplanation() {
    CedarAssertionResult assertion = new CedarAssertionResult("The object should be null")
        .parameter("name", "oldGroup");

    assertion.mergeErrorPack(new CedarErrorPack()
        .status(CedarResponseStatus.CONFLICT)
        .message("There is a group with the same name")
        .errorKey(CedarErrorKey.GROUP_ALREADY_PRESENT)
        .parameter("schema:name", "Group"));

    CedarErrorPack pack = assertion.getErrorPack();
    assertEquals(CedarResponseStatus.CONFLICT, pack.getStatus());
    assertEquals("There is a group with the same name", pack.getMessage());
    assertEquals(CedarErrorKey.GROUP_ALREADY_PRESENT, pack.getErrorKey());
    assertEquals("Group", pack.getParameters().get("schema:name"));
  }

  /**
   * The status, type, keys and suggested action all start as non-null defaults, so a merge that took
   * every non-null value overwrote the assertion's decisions with the caller's defaults: a 403 with a
   * key and a type came back as a 500 with neither, still marked as resolved.
   */
  @Test
  @DisplayName("Merging a caller's pack takes only what that pack set")
  void mergeTakesOnlyWhatTheCallersPackSet() {
    CedarAssertionResult assertion = new CedarAssertionResult("m")
        .errorKey(CedarErrorKey.GROUP_ALREADY_PRESENT)
        .errorReasonKey(CedarErrorReasonKey.SYSTEM_FOLDER)
        .forbidden();
    assertion.getErrorPack().errorType(CedarErrorType.PERMISSION).suggestedAction(CedarSuggestedAction.REQUEST_ROLE);

    assertion.mergeErrorPack(new CedarErrorPack().message("caller"));

    CedarErrorPack pack = assertion.getErrorPack();
    assertEquals(CedarResponseStatus.FORBIDDEN, pack.getStatus());
    assertTrue(pack.hasResolvedStatus());
    assertEquals(CedarErrorKey.GROUP_ALREADY_PRESENT, pack.getErrorKey());
    assertEquals(CedarErrorReasonKey.SYSTEM_FOLDER, pack.getErrorReasonKey());
    assertEquals(CedarErrorType.PERMISSION, pack.getErrorType());
    assertEquals(CedarSuggestedAction.REQUEST_ROLE, pack.getSuggestedAction());
    assertEquals("caller", pack.getMessage());
  }

  /**
   * A caller's type without a status is applied as {@link CedarErrorPack#errorType} applies it: it
   * supplies the status only when the assertion chose none.
   */
  @Test
  @DisplayName("A caller's error type supplies a status only where the assertion chose none")
  void aCallersErrorTypeSuppliesAStatusOnlyWhereNoneWasChosen() {
    CedarAssertionResult unstatused = new CedarAssertionResult("m");
    unstatused.mergeErrorPack(new CedarErrorPack().errorType(CedarErrorType.NOT_FOUND));
    assertEquals(CedarResponseStatus.NOT_FOUND, unstatused.getErrorPack().getStatus());
    assertEquals(CedarErrorType.NOT_FOUND, unstatused.getErrorPack().getErrorType());

    CedarAssertionResult forbidden = new CedarAssertionResult("m").forbidden();
    forbidden.mergeErrorPack(new CedarErrorPack().errorType(CedarErrorType.NOT_FOUND));
    assertEquals(CedarResponseStatus.FORBIDDEN, forbidden.getErrorPack().getStatus());
    assertEquals(CedarErrorType.NOT_FOUND, forbidden.getErrorPack().getErrorType());
  }
}
