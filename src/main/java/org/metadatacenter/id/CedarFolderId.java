package org.metadatacenter.id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CedarFolderId extends CedarFilesystemResourceId {

  private static final Logger log = LoggerFactory.getLogger(CedarFolderId.class);

  private CedarFolderId() {
  }

  protected CedarFolderId(String id) {
    super(id);
  }

  public static CedarFolderId build(String id) {
    return new CedarFolderId(id);
  }
}
