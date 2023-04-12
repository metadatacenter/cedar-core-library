package org.metadatacenter.id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CedarUntypedSchemaArtifactId extends CedarSchemaArtifactId {

  private static final Logger log = LoggerFactory.getLogger(CedarUntypedSchemaArtifactId.class);

  private CedarUntypedSchemaArtifactId() {
  }

  protected CedarUntypedSchemaArtifactId(String id) {
    super(id);
  }

  public static CedarUntypedSchemaArtifactId build(String id) {
    return new CedarUntypedSchemaArtifactId(id);
  }

}
