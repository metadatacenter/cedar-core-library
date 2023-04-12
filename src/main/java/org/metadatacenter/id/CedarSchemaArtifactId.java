package org.metadatacenter.id;

import org.metadatacenter.model.CedarResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CedarSchemaArtifactId extends CedarArtifactId {

  private static final Logger log = LoggerFactory.getLogger(CedarSchemaArtifactId.class);

  protected CedarSchemaArtifactId() {
  }

  protected CedarSchemaArtifactId(String id) {
    super(id);
  }

  public static CedarSchemaArtifactId build(String id, CedarResourceType type) {
    switch (type) {
      case FIELD:
        return CedarFieldId.build(id);
      case ELEMENT:
        return CedarElementId.build(id);
      case TEMPLATE:
        return CedarTemplateId.build(id);
    }
    log.error("Error creating CedarSchemaArtifactId, unrecognized type:" + type);
    return null;
  }

}
