package org.metadatacenter.id;

import org.metadatacenter.model.CedarResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CedarInstanceArtifactId extends CedarArtifactId {

  private static final Logger log = LoggerFactory.getLogger(CedarInstanceArtifactId.class);

  protected CedarInstanceArtifactId() {
  }

  protected CedarInstanceArtifactId(String id) {
    super(id);
  }

  public static CedarInstanceArtifactId build(String id, CedarResourceType type) {
    switch (type) {
      case INSTANCE:
        return CedarTemplateInstanceId.build(id);
    }
    log.error("Error creating CedarInstanceArtifactId, unrecognized type:" + type);
    return null;
  }

}
