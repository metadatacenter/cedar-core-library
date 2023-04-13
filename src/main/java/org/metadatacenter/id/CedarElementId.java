package org.metadatacenter.id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CedarElementId extends CedarSchemaArtifactId {

  private static final Logger log = LoggerFactory.getLogger(CedarElementId.class);

  private CedarElementId() {
  }

  protected CedarElementId(String id) {
    super(id);
  }

  public static CedarElementId build(String id) {
    return new CedarElementId(id);
  }
}
