package org.metadatacenter.id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CedarUntypedResourceId extends CedarResourceId {

  private static final Logger log = LoggerFactory.getLogger(CedarUntypedResourceId.class);

  private CedarUntypedResourceId() {
  }

  protected CedarUntypedResourceId(String id) {
    super(id);
  }

  public static CedarUntypedResourceId build(String id) {
    return new CedarUntypedResourceId(id);
  }

}
