package org.metadatacenter.id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CedarUntypedArtifactId extends CedarArtifactId {

  private static final Logger log = LoggerFactory.getLogger(CedarUntypedArtifactId.class);

  private CedarUntypedArtifactId() {
  }

  protected CedarUntypedArtifactId(String id) {
    super(id);
  }

  public static CedarUntypedArtifactId build(String id) {
    return new CedarUntypedArtifactId(id);
  }

}
