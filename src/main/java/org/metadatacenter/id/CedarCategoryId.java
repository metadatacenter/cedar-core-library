package org.metadatacenter.id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CedarCategoryId extends CedarResourceId {

  public final static String CATEGORY_ID_ROOT = "root";
  private static final Logger log = LoggerFactory.getLogger(CedarCategoryId.class);

  private CedarCategoryId() {
  }

  protected CedarCategoryId(String id) {
    super(id);
  }

  public static CedarCategoryId build(String id) {
    return new CedarCategoryId(id);
  }
}
