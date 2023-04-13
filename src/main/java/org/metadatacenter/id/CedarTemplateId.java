package org.metadatacenter.id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CedarTemplateId extends CedarSchemaArtifactId {

  private static final Logger log = LoggerFactory.getLogger(CedarTemplateId.class);

  private CedarTemplateId() {
  }

  protected CedarTemplateId(String id) {
    super(id);
  }

  public static CedarTemplateId build(String id) {
    return new CedarTemplateId(id);
  }

}
