package org.metadatacenter.exception;

import org.metadatacenter.http.CedarResponseStatus;

public class CedarObjectNotFoundException extends CedarException {

  public CedarObjectNotFoundException(String message) {
    super(message);
    errorPack.status(CedarResponseStatus.NOT_FOUND);
  }
}
