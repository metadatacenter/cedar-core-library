package org.metadatacenter.exception;

import org.metadatacenter.http.CedarResponseStatus;

/**
 * A failure in the server's own work: an I/O error, a store that answered badly, a state the code did
 * not expect. It is a 500 by declaration rather than by falling through to the error pack's default,
 * so the pack reports a decided status and a site that wants another one chains it, as
 * {@link CedarDependencyUnavailableException} does for 503 and {@link CedarException#badRequest()}
 * for a request the processing exposed as wrong.
 */
public class CedarProcessingException extends CedarException {

  public CedarProcessingException(String message, Exception sourceException) {
    super(message, sourceException);
    errorPack.status(CedarResponseStatus.INTERNAL_SERVER_ERROR);
  }

  public CedarProcessingException(Exception sourceException) {
    super(sourceException);
    errorPack.status(CedarResponseStatus.INTERNAL_SERVER_ERROR);
  }

  public CedarProcessingException(String message) {
    super(message);
    errorPack.status(CedarResponseStatus.INTERNAL_SERVER_ERROR);
  }

}
