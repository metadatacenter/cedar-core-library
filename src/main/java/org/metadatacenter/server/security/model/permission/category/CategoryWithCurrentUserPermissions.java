package org.metadatacenter.server.security.model.permission.category;

import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.id.CedarCategoryId;
import org.metadatacenter.model.CedarResourceType;
import org.metadatacenter.server.security.model.auth.CurrentUserCategoryPermissions;

public interface CategoryWithCurrentUserPermissions {

  String getId();

  CedarResourceType getType();

  CedarCategoryId getResourceId() throws CedarProcessingException;

  boolean isRoot();

  CurrentUserCategoryPermissions getCurrentUserPermissions();
}
