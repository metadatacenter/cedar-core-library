package org.metadatacenter.server.security.model;

import org.metadatacenter.id.CedarFilesystemResourceId;
import org.metadatacenter.model.CedarResourceType;

public interface FilesystemResourceWithIdAndType<T extends CedarFilesystemResourceId> {

  String getId();

  CedarResourceType getType();

  T getResourceId();

}
