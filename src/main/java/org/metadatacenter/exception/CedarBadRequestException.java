package org.metadatacenter.exception;

import org.metadatacenter.error.CedarErrorPack;

import javax.ws.rs.core.Response;

public class CedarBadRequestException extends CedarException {

  public CedarBadRequestException(CedarErrorPack ep) {
    super(ep);
    errorPack.status(Response.Status.BAD_REQUEST);
  }

  public CedarBadRequestException(String message, Exception e) {
    super(message + " : " + e.getMessage());
    errorPack.status(Response.Status.BAD_REQUEST);
    errorPack.sourceException(e);
  }
}
