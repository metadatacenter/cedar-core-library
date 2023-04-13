package org.metadatacenter.error;

public class CedarFixedRecursiveException extends Exception {
  public CedarFixedRecursiveException(Throwable throwable) {
    super(throwable.getMessage());
    this.setStackTrace(throwable.getStackTrace());
    this.initCause(null);
  }
}
