package org.metadatacenter.exception;

import javax.ws.rs.core.Response;

public class CedarPermissionException extends CedarException {

  public CedarPermissionException(String message) {
    super(message);
    errorPack.status(Response.Status.UNAUTHORIZED);
  }

}
