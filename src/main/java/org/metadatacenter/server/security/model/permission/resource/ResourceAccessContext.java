package org.metadatacenter.server.security.model.permission.resource;

import org.metadatacenter.model.CedarResourceType;

/** Resource facts that affect capability evaluation independently of grants and ownership. */
public record ResourceAccessContext(CedarResourceType resourceType, boolean protectedFolder) {

  public static ResourceAccessContext ordinary(CedarResourceType resourceType) {
    return new ResourceAccessContext(resourceType, false);
  }
}
