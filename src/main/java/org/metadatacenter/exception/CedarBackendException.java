package org.metadatacenter.exception;

import org.metadatacenter.error.CedarAssertionResult;
import org.metadatacenter.error.CedarErrorPack;
import org.metadatacenter.http.CedarResponseStatus;
import org.metadatacenter.server.result.BackendCallError;
import org.metadatacenter.server.result.BackendCallResult;

public class CedarBackendException extends CedarException {

  public CedarBackendException(BackendCallResult backendCallResult) {
    super(backendCallResult.getFirstErrorMessage());
    BackendCallError firstError = backendCallResult.getFirstError();
    if (firstError != null) {
      this.errorPack = new CedarErrorPack(firstError.getErrorPack());
    } else {
      // A backend call that failed without recording why is the server's fault, and saying so here
      // is what keeps the mapper from flagging this pack as one nobody statused.
      errorPack.status(CedarResponseStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public CedarBackendException(CedarAssertionResult ar) {
    super(ar.getErrorPack());
  }
}
