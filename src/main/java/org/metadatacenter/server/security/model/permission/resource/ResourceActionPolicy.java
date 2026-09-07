package org.metadatacenter.server.security.model.permission.resource;

import org.metadatacenter.model.CedarResourceType;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import static org.metadatacenter.server.security.model.permission.resource.ResourceAction.*;
import static org.metadatacenter.server.security.model.permission.resource.ResourceCapability.*;

/** Calculates actions whose availability follows directly from resource capabilities and state. */
public final class ResourceActionPolicy {

  private ResourceActionPolicy() {
  }

  public static Set<ResourceAction> evaluate(CedarResourceType resourceType,
                                             Set<ResourceCapability> capabilities,
                                             Boolean open) {
    if (resourceType == null || capabilities == null) {
      return Collections.emptySet();
    }
    EnumSet<ResourceAction> actions = EnumSet.noneOf(ResourceAction.class);
    if (resourceType != CedarResourceType.FOLDER && capabilities.contains(READ_RESOURCE)) {
      actions.add(COPY_FROM_RESOURCE);
    }
    if (resourceType == CedarResourceType.TEMPLATE && capabilities.contains(READ_RESOURCE)) {
      actions.add(POPULATE);
    }
    if (open != null && capabilities.contains(MANAGE_OPENVIEW)) {
      actions.add(open ? DISABLE_OPENVIEW : ENABLE_OPENVIEW);
    }
    return Collections.unmodifiableSet(actions);
  }
}
