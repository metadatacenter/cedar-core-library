package org.metadatacenter.server.security.model.auth;

import org.metadatacenter.server.security.model.NodeWithPublicationStatus;

public interface FilesystemResourceWithCurrentUserPermissionsAndPublicationStatus extends FilesystemResourceWithCurrentUserPermissions,
    NodeWithPublicationStatus {
}
