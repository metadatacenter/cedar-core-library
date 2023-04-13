package org.metadatacenter.id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CedarFieldId extends CedarSchemaArtifactId {

  private static final Logger log = LoggerFactory.getLogger(CedarFieldId.class);

  private CedarFieldId() {
  }

  protected CedarFieldId(String id) {
    super(id);
  }

  public static CedarFieldId build(String id) {
    return new CedarFieldId(id);
  }
}
