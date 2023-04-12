package org.metadatacenter.id;

import org.metadatacenter.model.CedarResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CedarArtifactId extends CedarFilesystemResourceId {

  private static final Logger log = LoggerFactory.getLogger(CedarArtifactId.class);

  protected CedarArtifactId() {
  }

  protected CedarArtifactId(String id) {
    super(id);
  }

  public static CedarArtifactId build(String id, CedarResourceType type) {
    switch (type) {
      case FIELD:
        return new CedarFieldId(id);
      case ELEMENT:
        return new CedarElementId(id);
      case TEMPLATE:
        return new CedarTemplateId(id);
      case INSTANCE:
        return new CedarTemplateInstanceId(id);
    }
    log.error("Error creating CedarArtifactId, unrecognized type:" + type);
    return null;
  }

}
