package org.metadatacenter.exception;

import org.metadatacenter.error.CedarErrorKey;
import org.metadatacenter.error.CedarErrorPack;

public class CedarRequestBodyMissingFieldException extends CedarBadRequestException {

  public CedarRequestBodyMissingFieldException(String pointer) {
    super(new CedarErrorPack()
        .message("The request body must include a non-empty '" + pointer + "' field")
        .errorKey(CedarErrorKey.INVALID_INPUT)
    );
  }

  public CedarRequestBodyMissingFieldException(String pointer, CedarErrorKey errorKey) {
    this(pointer);
    errorPack.errorKey(errorKey);
  }
}
