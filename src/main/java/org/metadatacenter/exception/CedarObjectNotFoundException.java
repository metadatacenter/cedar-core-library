package org.metadatacenter.exception;

import javax.ws.rs.core.Response;

public class CedarObjectNotFoundException extends CedarException {

  public CedarObjectNotFoundException(String message) {
    super(message);
    errorPack.status(Response.Status.NOT_FOUND);
  }
}
