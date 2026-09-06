package org.metadatacenter.server.security.model.permission.category;

/** Category state that can restrict otherwise available capabilities. */
public record CategoryAccessContext(boolean root) {
}
