package org.metadatacenter.exception;

import org.metadatacenter.http.CedarResponseStatus;

public class CedarPermissionException extends CedarException {

  public CedarPermissionException(String message) {
    super(message);
    errorPack.status(CedarResponseStatus.UNAUTHORIZED);
  }

}
