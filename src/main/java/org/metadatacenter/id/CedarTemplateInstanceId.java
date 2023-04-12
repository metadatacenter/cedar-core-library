package org.metadatacenter.id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CedarTemplateInstanceId extends CedarInstanceArtifactId {

  private static final Logger log = LoggerFactory.getLogger(CedarTemplateInstanceId.class);

  private CedarTemplateInstanceId() {
  }

  protected CedarTemplateInstanceId(String id) {
    super(id);
  }

  public static CedarTemplateInstanceId build(String id) {
    return new CedarTemplateInstanceId(id);
  }
}
