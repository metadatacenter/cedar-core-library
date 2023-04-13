package org.metadatacenter.id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CedarUntypedFilesystemResourceId extends CedarFilesystemResourceId {

  private static final Logger log = LoggerFactory.getLogger(CedarUntypedFilesystemResourceId.class);

  protected CedarUntypedFilesystemResourceId() {
  }

  protected CedarUntypedFilesystemResourceId(String id) {
    super(id);
  }

  public static CedarUntypedFilesystemResourceId build(String id) {
    return new CedarUntypedFilesystemResourceId(id);
  }

}
