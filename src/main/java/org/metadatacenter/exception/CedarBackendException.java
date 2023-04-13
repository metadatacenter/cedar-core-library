package org.metadatacenter.exception;

import org.metadatacenter.error.CedarAssertionResult;
import org.metadatacenter.error.CedarErrorPack;
import org.metadatacenter.server.result.BackendCallError;
import org.metadatacenter.server.result.BackendCallResult;

public class CedarBackendException extends CedarException {

  public CedarBackendException(BackendCallResult backendCallResult) {
    super(backendCallResult.getFirstErrorMessage());
    BackendCallError firstError = backendCallResult.getFirstError();
    if (firstError != null) {
      this.errorPack = new CedarErrorPack(firstError.getErrorPack());
    }
  }

  public CedarBackendException(CedarAssertionResult ar) {
    super(ar.getErrorPack());
  }
}
