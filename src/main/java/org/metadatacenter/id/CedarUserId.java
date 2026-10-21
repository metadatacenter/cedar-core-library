package org.metadatacenter.id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CedarUserId extends CedarResourceId {

  private static final Logger log = LoggerFactory.getLogger(CedarUserId.class);

  private CedarUserId() {
  }

  protected CedarUserId(String id) {
    super(id);
  }

  public static CedarUserId build(String id) {
    return new CedarUserId(id);
  }

}
