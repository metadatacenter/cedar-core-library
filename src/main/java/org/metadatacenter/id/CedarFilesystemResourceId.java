package org.metadatacenter.id;

import org.metadatacenter.model.CedarResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CedarFilesystemResourceId extends CedarResourceId {

  private static final Logger log = LoggerFactory.getLogger(CedarFilesystemResourceId.class);

  protected CedarFilesystemResourceId() {
  }

  protected CedarFilesystemResourceId(String id) {
    super(id);
  }

  public static CedarFilesystemResourceId build(String id, CedarResourceType type) {
    switch (type) {
      case FOLDER:
        return CedarFolderId.build(id);
      case FIELD:
        return CedarFieldId.build(id);
      case ELEMENT:
        return CedarElementId.build(id);
      case TEMPLATE:
        return CedarTemplateId.build(id);
      case INSTANCE:
        return CedarTemplateInstanceId.build(id);
    }
    log.error("Error creating CedarFilesystemResourceId, unrecognized type:" + type);
    return null;
  }

  public CedarFolderId asFolderId() {
    return (CedarFolderId) this;
  }

  public CedarArtifactId asArtifactId() {
    return (CedarArtifactId) this;
  }

}
