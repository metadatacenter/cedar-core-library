package org.metadatacenter.exception.security;

import org.metadatacenter.error.CedarErrorKey;
import org.metadatacenter.http.CedarResponseStatus;

public class PermissionNotOwnedException extends CedarAccessException {

  public PermissionNotOwnedException(String permissionName) {
    super("The current actor does not own the required permission: '" + permissionName + "'.",
        CedarErrorKey.PERMISSION_NOT_OWNED, null, null);
    errorPack.parameter("permissionName", permissionName);
    errorPack.status(CedarResponseStatus.FORBIDDEN);
  }
}
