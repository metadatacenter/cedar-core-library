package org.metadatacenter.error;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.ws.rs.core.Response;

public enum CedarErrorType {

  NONE(null, Response.Status.OK),
  NOT_FOUND("notFound", Response.Status.NOT_FOUND),
  INVALID_ARGUMENT("invalidArgument", Response.Status.BAD_REQUEST),
  AUTHORIZATION("authorization", Response.Status.UNAUTHORIZED),
  SERVER_ERROR("server", Response.Status.INTERNAL_SERVER_ERROR),
  VALIDATION_ERROR("validationError", Response.Status.BAD_REQUEST);

  private final String value;
  private final Response.Status status;

  CedarErrorType(String value, Response.Status status) {
    this.value = value;
    this.status = status;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  public Response.Status getStatus() {
    return status;
  }
}
