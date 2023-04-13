package org.metadatacenter.exception;

import org.metadatacenter.error.CedarErrorKey;
import org.metadatacenter.error.CedarErrorPack;
import org.metadatacenter.error.CedarErrorPackException;
import org.metadatacenter.error.CedarErrorReasonKey;

import javax.ws.rs.core.Response;

public abstract class CedarException extends Exception {

  protected CedarErrorPack errorPack;
  protected boolean showFullStackTrace = false;

  private CedarException() {
  }

  public CedarException(String message, Exception sourceException) {
    super(message, sourceException);
    errorPack = new CedarErrorPack();
    errorPack.message(message);
    if (sourceException != null) {
      errorPack.sourceException(sourceException);
    } else {
      errorPack.sourceException(new CedarHelperException());
    }
  }

  public CedarException(String message, CedarErrorPackException errorPackException) {
    super(message);
    errorPack = new CedarErrorPack();
    errorPack.message(message);
    if (errorPackException != null) {
      errorPack.sourceException(errorPackException);
    } else {
      errorPack.sourceException(new CedarHelperException());
    }
  }

  protected CedarException(String message) {
    this(message, (Exception) null);
  }

  public CedarException(Exception sourceException) {
    this(sourceException.getMessage(), sourceException);
  }

  public CedarException(CedarErrorPack errorPack) {
    this(errorPack.getMessage(), errorPack.getSourceException());
    this.errorPack = new CedarErrorPack(errorPack);
  }

  public CedarErrorPack getErrorPack() {
    return errorPack;
  }

  public CedarException errorKey(CedarErrorKey errorKey) {
    errorPack.errorKey(errorKey);
    return this;
  }

  public CedarException errorReasonKey(CedarErrorReasonKey errorReasonKey) {
    errorPack.errorReasonKey(errorReasonKey);
    return this;
  }

  public CedarException parameter(String name, Object value) {
    errorPack.parameter(name, value);
    return this;
  }

  public boolean isShowFullStackTrace() {
    return showFullStackTrace;
  }

  public CedarException badRequest() {
    errorPack.status(Response.Status.BAD_REQUEST);
    return this;
  }
}
