package org.metadatacenter.exception;

import org.metadatacenter.http.CedarResponseStatus;

/**
 * Thrown when an authenticated user is denied by a resource-level permission check. Reported as
 * 403 Forbidden: 401 is reserved for requests whose credentials are missing or invalid.
 */
public class CedarPermissionException extends CedarException {

  public CedarPermissionException(String message) {
    super(message);
    errorPack.status(CedarResponseStatus.FORBIDDEN);
  }

}
