package org.metadatacenter.id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CedarElementInstanceId extends CedarSchemaArtifactId {

  private static final Logger log = LoggerFactory.getLogger(CedarElementInstanceId.class);

  private CedarElementInstanceId() {
  }

  protected CedarElementInstanceId(String id) {
    super(id);
  }

  public static CedarElementInstanceId build(String id) {
    return new CedarElementInstanceId(id);
  }
}
