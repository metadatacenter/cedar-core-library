package org.metadatacenter.error;

import org.metadatacenter.http.CedarResponseStatus;
import org.metadatacenter.server.result.BackendCallError;
import org.metadatacenter.server.result.BackendCallResult;

public class CedarAssertionResult {

  private CedarErrorPack errorPack;

  public CedarAssertionResult(String message) {
    this.errorPack = new CedarErrorPack();
    errorPack.message(message);
  }

  public CedarAssertionResult(BackendCallResult backendCallResult) {
    this(backendCallResult.getFirstErrorMessage());
    BackendCallError firstError = backendCallResult.getFirstError();
    if (firstError != null) {
      errorPack = new CedarErrorPack(firstError.getErrorPack());
    }
  }

  public CedarAssertionResult internalServerError() {
    errorPack.status(CedarResponseStatus.INTERNAL_SERVER_ERROR);
    return this;
  }

  public CedarAssertionResult forbidden() {
    errorPack.status(CedarResponseStatus.FORBIDDEN);
    return this;
  }

  public CedarAssertionResult unauthorized() {
    errorPack.status(CedarResponseStatus.UNAUTHORIZED);
    return this;
  }

  public CedarAssertionResult notFound() {
    errorPack.status(CedarResponseStatus.NOT_FOUND);
    return this;
  }

  public CedarAssertionResult badRequest() {
    errorPack.status(CedarResponseStatus.BAD_REQUEST);
    return this;
  }

  public CedarAssertionResult parameter(String name, Object value) {
    this.errorPack.parameter(name, value);
    return this;
  }

  public CedarAssertionResult entity(String name, Object value) {
    this.errorPack.entity(name, value);
    return this;
  }

  public CedarAssertionResult message(String message) {
    this.errorPack.message(message);
    return this;
  }

  public CedarAssertionResult status(CedarResponseStatus status) {
    this.errorPack.status(status);
    return this;
  }

  public CedarAssertionResult errorType(CedarErrorType errorType) {
    this.errorPack.errorType(errorType);
    return this;
  }

  public CedarAssertionResult errorKey(CedarErrorKey errorKey) {
    this.errorPack.errorKey(errorKey);
    return this;
  }

  public CedarAssertionResult errorReasonKey(CedarErrorReasonKey errorReasonKey) {
    this.errorPack.errorReasonKey(errorReasonKey);
    return this;
  }

  public CedarErrorPack getErrorPack() {
    return errorPack;
  }

  public void mergeErrorPack(CedarErrorPack errorPack) {
    this.errorPack.merge(errorPack);
  }
}
