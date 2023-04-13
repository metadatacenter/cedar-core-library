package org.metadatacenter.server.result;

import org.metadatacenter.error.*;
import org.metadatacenter.operation.CedarOperationDescriptor;

public class BackendCallError {

  private final CedarErrorPack errorPack;

  BackendCallError(CedarErrorType errorType) {
    errorPack = new CedarErrorPack();
    errorPack.errorType(errorType);
    errorPack.status(errorType.getStatus());
  }

  public BackendCallError message(String message) {
    errorPack.message(message);
    return this;
  }

  public BackendCallError parameter(String name, Object value) {
    errorPack.parameter(name, value);
    return this;
  }

  public BackendCallError suggestedAction(CedarSuggestedAction suggestedAction) {
    errorPack.suggestedAction(suggestedAction);
    return this;
  }

  public BackendCallError errorKey(CedarErrorKey errorKey) {
    errorPack.errorKey(errorKey);
    return this;
  }

  public BackendCallError errorReasonKey(CedarErrorReasonKey errorReasonKey) {
    errorPack.errorReasonKey(errorReasonKey);
    return this;
  }

  public BackendCallError operation(CedarOperationDescriptor operation) {
    errorPack.operation(operation);
    return this;
  }

  public BackendCallError sourceException(Exception e) {
    errorPack.sourceException(e);
    return this;
  }

  public CedarErrorPack getErrorPack() {
    return errorPack;
  }
}
