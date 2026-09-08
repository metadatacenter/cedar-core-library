package org.metadatacenter.server.result;

import org.metadatacenter.error.CedarErrorType;

import java.util.ArrayList;
import java.util.List;

public class BackendCallResult<T> {

  private final List<BackendCallError> errors;

  private T payload;

  public BackendCallResult() {
    this.errors = new ArrayList<>();
  }

  public void setPayload(T payload) {
    this.payload = payload;
  }

  public T getPayload() {
    return payload;
  }

  public boolean isOk() {
    return !isError();
  }

  public boolean isError() {
    return !errors.isEmpty();
  }

  public BackendCallError addError(CedarErrorType type) {
    BackendCallError e = new BackendCallError(type);
    errors.add(e);
    return e;
  }

  // Both answer null for a result that recorded no error. They used to index into the empty list,
  // so wrapping such a result in a CedarBackendException died with an IndexOutOfBoundsException
  // before the exception could say anything.
  public String getFirstErrorMessage() {
    BackendCallError firstError = getFirstError();
    return firstError == null ? null : firstError.getErrorPack().getMessage();
  }

  public BackendCallError getFirstError() {
    if (errors == null || errors.isEmpty()) {
      return null;
    }
    return errors.get(0);
  }
}
