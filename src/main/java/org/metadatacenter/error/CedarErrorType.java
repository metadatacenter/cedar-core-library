package org.metadatacenter.error;

import com.fasterxml.jackson.annotation.JsonValue;
import org.metadatacenter.http.CedarResponseStatus;

public enum CedarErrorType {

  NONE(null, CedarResponseStatus.OK),
  NOT_FOUND("notFound", CedarResponseStatus.NOT_FOUND),
  INVALID_ARGUMENT("invalidArgument", CedarResponseStatus.BAD_REQUEST),
  // AUTHENTICATION failure: the caller is not identified (no or bad credentials), so 401 asks them to
  // authenticate. PERMISSION failure: the caller is identified but lacks rights on the resource, so 403.
  // Keep the two apart — a permission denial reported as 401 wrongly tells a client to re-authenticate.
  AUTHORIZATION("authorization", CedarResponseStatus.UNAUTHORIZED),
  PERMISSION("permission", CedarResponseStatus.FORBIDDEN),
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
