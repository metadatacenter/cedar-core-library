package org.metadatacenter.error;

import org.junit.jupiter.api.Test;
import org.metadatacenter.operation.CedarOperationDescriptor;
import org.metadatacenter.operation.CedarOperations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * A merge takes what the other pack set and keeps everything else. The maps are replaced as a whole
 * rather than combined, so a merged pack never mixes the parameters of two errors.
 */
class CedarErrorPackMergeTest {

  private static final CedarOperationDescriptor EARLIER = CedarOperations.lookup(String.class, "id", "earlier");
  private static final CedarOperationDescriptor LATER = CedarOperations.lookup(String.class, "id", "later");

  private static CedarErrorPack earlierPack(Exception cause) {
    return new CedarErrorPack()
        .errorReasonKey(CedarErrorReasonKey.SYSTEM_FOLDER)
        .message("earlier")
        .parameter("p", "earlier")
        .object("o", "earlier")
        .entity("e", "earlier")
        .suggestedAction(CedarSuggestedAction.LOGOUT)
        .sourceException(cause)
        .operation(EARLIER);
  }

  @Test
  void aMergeTakesEverythingTheOtherPackSet() {
    CedarErrorPack pack = earlierPack(new IllegalStateException("earlier"));
    Exception laterCause = new IllegalArgumentException("later");
    CedarErrorPack later = new CedarErrorPack()
        .errorReasonKey(CedarErrorReasonKey.NON_EMPTY_FOLDER)
        .message("later")
        .parameter("q", "later")
        .object("r", "later")
        .entity("s", "later")
        .suggestedAction(CedarSuggestedAction.REQUEST_ROLE)
        .sourceException(laterCause)
        .operation(LATER);

    pack.merge(later);

    assertEquals(CedarErrorReasonKey.NON_EMPTY_FOLDER, pack.getErrorReasonKey());
    assertEquals("later", pack.getMessage());
    assertEquals(Map.of("q", "later"), pack.getParameters());
    assertEquals(Map.of("r", "later"), pack.getObjects());
    assertEquals(Map.of("s", "later"), pack.getEntities());
    assertEquals(CedarSuggestedAction.REQUEST_ROLE, pack.getSuggestedAction());
    assertSame(laterCause, pack.getOriginalException());
    assertSame(later.getSourceException(), pack.getSourceException());
    assertSame(LATER, pack.getOperation());
  }

  @Test
  void aMergeKeepsWhatTheOtherPackLeftUnset() {
    Exception cause = new IllegalStateException("earlier");
    CedarErrorPack pack = earlierPack(cause);
    var sourceException = pack.getSourceException();

    pack.merge(new CedarErrorPack());

    assertEquals(CedarErrorReasonKey.SYSTEM_FOLDER, pack.getErrorReasonKey());
    assertEquals("earlier", pack.getMessage());
    assertEquals(Map.of("p", "earlier"), pack.getParameters());
    assertEquals(Map.of("o", "earlier"), pack.getObjects());
    assertEquals(Map.of("e", "earlier"), pack.getEntities());
    assertEquals(CedarSuggestedAction.LOGOUT, pack.getSuggestedAction());
    assertSame(cause, pack.getOriginalException());
    assertSame(sourceException, pack.getSourceException());
    assertSame(EARLIER, pack.getOperation());
  }
}
