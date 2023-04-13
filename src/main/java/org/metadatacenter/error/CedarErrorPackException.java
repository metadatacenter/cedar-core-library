package org.metadatacenter.error;

import java.util.ArrayList;
import java.util.List;

public class CedarErrorPackException extends CedarErrorPackSingleException {

  private final List<CedarErrorPackSingleException> causes;

  public CedarErrorPackException(Exception e) {
    causes = new ArrayList<>();
    if (e != null) {
      this.message = e.getMessage();
      this.localizedMessage = e.getLocalizedMessage();
      this.stackTrace = e.getStackTrace();
      appendCause(e.getCause());
    }
  }

  private void appendCause(Throwable cause) {
    if (cause != null) {
      CedarErrorPackSingleException c = new CedarErrorPackSingleException();
      c.message = cause.getMessage();
      c.localizedMessage = cause.getLocalizedMessage();
      c.stackTrace = cause.getStackTrace();
      causes.add(c);
      appendCause(cause.getCause());
    }
  }

  public List<CedarErrorPackSingleException> getCauses() {
    return causes;
  }
}
