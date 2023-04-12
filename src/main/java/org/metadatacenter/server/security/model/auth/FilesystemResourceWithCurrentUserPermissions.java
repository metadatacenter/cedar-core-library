package org.metadatacenter.server.security.model.auth;

import org.metadatacenter.id.CedarFilesystemResourceId;
import org.metadatacenter.server.security.model.FilesystemResourceWithIdAndType;

public interface FilesystemResourceWithCurrentUserPermissions<T extends CedarFilesystemResourceId> extends FilesystemResourceWithIdAndType<T> {

  CurrentUserResourcePermissions getCurrentUserPermissions();
}
