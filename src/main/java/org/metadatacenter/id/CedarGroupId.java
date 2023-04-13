package org.metadatacenter.id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CedarGroupId extends CedarResourceId {

  private static final Logger log = LoggerFactory.getLogger(CedarGroupId.class);

  private CedarGroupId() {
  }

  protected CedarGroupId(String id) {
    super(id);
  }

  public static CedarGroupId build(String id) {
    return new CedarGroupId(id);
  }

}
