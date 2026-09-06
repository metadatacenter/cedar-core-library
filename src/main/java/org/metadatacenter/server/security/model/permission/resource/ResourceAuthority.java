package org.metadatacenter.server.security.model.permission.resource;

import org.metadatacenter.model.CedarResourceType;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/** The role and ownership that apply to one user on one artifact or folder. */
public record ResourceAuthority(ResourceRole role, boolean owner) {

  /** Highest role requirement this authority satisfies; ownership itself remains separate from role. */
  public ResourceRole highestSatisfiedRole() {
    return owner ? ResourceRole.MANAGER : role;
  }

  public boolean satisfies(ResourceRole requiredRole) {
    ResourceRole satisfiedRole = highestSatisfiedRole();
    return satisfiedRole != null && satisfiedRole.includes(requiredRole);
  }

  public boolean provides(CedarResourceType resourceType, ResourceCapability capability) {
    if (capability == null || !capability.appliesTo(resourceType)) {
      return false;
    }
    if (capability == ResourceCapability.TRANSFER_OWNERSHIP) {
      return owner;
    }
    ResourceRole satisfiedRole = highestSatisfiedRole();
    return satisfiedRole != null && satisfiedRole.provides(resourceType, capability);
  }

  /** Capabilities supplied by this authority that are meaningful for the resource type. */
  public Set<ResourceCapability> capabilitiesFor(CedarResourceType resourceType) {
    EnumSet<ResourceCapability> capabilities = EnumSet.noneOf(ResourceCapability.class);
    for (ResourceCapability capability : ResourceCapability.values()) {
      if (provides(resourceType, capability)) {
        capabilities.add(capability);
      }
    }
    return Collections.unmodifiableSet(capabilities);
  }
}
