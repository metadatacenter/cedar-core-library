package org.metadatacenter.error;

import com.fasterxml.jackson.annotation.JsonValue;
import org.metadatacenter.http.CedarResponseStatus;

public enum CedarErrorType {

  NONE(null, CedarResponseStatus.OK),
  NOT_FOUND("notFound", CedarResponseStatus.NOT_FOUND),
  INVALID_ARGUMENT("invalidArgument", CedarResponseStatus.BAD_REQUEST),
  AUTHORIZATION("authorization", CedarResponseStatus.UNAUTHORIZED),
  SERVER_ERROR("server", CedarResponseStatus.INTERNAL_SERVER_ERROR),
  VALIDATION_ERROR("validationError", CedarResponseStatus.BAD_REQUEST);

  private final String value;
  private final CedarResponseStatus status;

  CedarErrorType(String value, CedarResponseStatus status) {
    this.value = value;
    this.status = status;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  public CedarResponseStatus getStatus() {
    return status;
  }
}
