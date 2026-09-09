package org.metadatacenter.error;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.metadatacenter.http.CedarResponseStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A pack's 500 can be a decision or a fallthrough, and the two used to be indistinguishable: the
 * field initialiser is INTERNAL_SERVER_ERROR, so a site that forgot to status its pack reported a
 * client's mistake as a server fault with nothing to tell the mapper it had forgotten.
 */
class CedarErrorPackStatusResolutionTest {

  @Test
  @DisplayName("A fresh pack reports 500 but admits nobody chose it")
  void freshPackHasNoResolvedStatus() {
    CedarErrorPack pack = new CedarErrorPack();
    assertEquals(CedarResponseStatus.INTERNAL_SERVER_ERROR, pack.getStatus());
    assertFalse(pack.hasResolvedStatus());
  }

  @Test
  @DisplayName("Choosing a status resolves it, a deliberate 500 included")
  void chosenStatusIsResolved() {
    assertTrue(new CedarErrorPack().status(CedarResponseStatus.BAD_REQUEST).hasResolvedStatus());
    assertTrue(new CedarErrorPack().status(CedarResponseStatus.INTERNAL_SERVER_ERROR).hasResolvedStatus());
  }

  @Test
  @DisplayName("An error type that implies a status resolves it")
  void derivedStatusIsResolved() {
    CedarErrorPack pack = new CedarErrorPack().errorType(CedarErrorType.INVALID_ARGUMENT);
    assertEquals(CedarResponseStatus.BAD_REQUEST, pack.getStatus());
    assertTrue(pack.hasResolvedStatus());
  }

  @Test
  @DisplayName("The NONE type decides nothing")
  void noneTypeResolvesNothing() {
    assertFalse(new CedarErrorPack().errorType(CedarErrorType.NONE).hasResolvedStatus());
  }

  @Test
  @DisplayName("A copy carries the resolution with the status")
  void copyCarriesResolution() {
    CedarErrorPack chosen = new CedarErrorPack().status(CedarResponseStatus.NOT_FOUND);
    assertTrue(new CedarErrorPack(chosen).hasResolvedStatus());
    assertFalse(new CedarErrorPack(new CedarErrorPack()).hasResolvedStatus());
  }
}
