package org.metadatacenter.server.security.model.permission.category;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/** The role and ownership that apply to one user on one category. */
public record CategoryAuthority(CategoryRole role, boolean owner) {

  public CategoryRole highestSatisfiedRole() {
    return owner ? CategoryRole.MANAGER : role;
  }

  public boolean satisfies(CategoryRole requiredRole) {
    CategoryRole satisfiedRole = highestSatisfiedRole();
    return satisfiedRole != null && satisfiedRole.includes(requiredRole);
  }

  public boolean provides(CategoryCapability capability) {
    if (capability == CategoryCapability.TRANSFER_OWNERSHIP) {
      return owner;
    }
    CategoryRole satisfiedRole = highestSatisfiedRole();
    return satisfiedRole != null && satisfiedRole.provides(capability);
  }

  public Set<CategoryCapability> capabilities() {
    EnumSet<CategoryCapability> capabilities = EnumSet.noneOf(CategoryCapability.class);
    for (CategoryCapability capability : CategoryCapability.values()) {
      if (provides(capability)) {
        capabilities.add(capability);
      }
    }
    return Collections.unmodifiableSet(capabilities);
  }
}
