package org.metadatacenter.exception;

public class CedarProcessingException extends CedarException {

  public CedarProcessingException(String message, Exception sourceException) {
    super(message, sourceException);
  }

  public CedarProcessingException(Exception sourceException) {
    super(sourceException);
  }

  public CedarProcessingException(String message) {
    super(message);
  }

}
