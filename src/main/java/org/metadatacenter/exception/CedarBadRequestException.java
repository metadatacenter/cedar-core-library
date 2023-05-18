package org.metadatacenter.exception;

import org.metadatacenter.error.CedarErrorPack;
import org.metadatacenter.http.CedarResponseStatus;

public class CedarBadRequestException extends CedarException {

  public CedarBadRequestException(CedarErrorPack ep) {
    super(ep);
    errorPack.status(CedarResponseStatus.BAD_REQUEST);
  }

  public CedarBadRequestException(String message, Exception e) {
    super(message + " : " + e.getMessage());
    errorPack.status(CedarResponseStatus.BAD_REQUEST);
    errorPack.sourceException(e);
  }
}
