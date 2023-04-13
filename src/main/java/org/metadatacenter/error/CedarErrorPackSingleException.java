package org.metadatacenter.error;

public class CedarErrorPackSingleException {

  protected StackTraceElement[] stackTrace;
  protected String message;
  protected String localizedMessage;

  public StackTraceElement[] getStackTrace() {
    return stackTrace;
  }

  public String getMessage() {
    return message;
  }

  public String getLocalizedMessage() {
    return localizedMessage;
  }

}
