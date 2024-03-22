package org.metadatacenter.id;

import org.metadatacenter.model.CedarResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CedarTypedSchemaArtifactId extends CedarSchemaArtifactId {

  private static final Logger log = LoggerFactory.getLogger(CedarTypedSchemaArtifactId.class);

  private CedarResourceType type;

  private CedarTypedSchemaArtifactId() {
  }

  protected CedarTypedSchemaArtifactId(CedarResourceType type, String id) {
    super(id);
    this.type = type;
  }

  public static CedarTypedSchemaArtifactId build(CedarResourceType type, String id) {
    return new CedarTypedSchemaArtifactId(type, id);
  }

  public CedarResourceType getType() {
    return type;
  }
}
