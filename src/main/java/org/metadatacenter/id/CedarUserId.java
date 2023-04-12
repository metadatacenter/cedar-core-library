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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CedarUserId that = (CedarUserId) o;

    return !(getId() != null ? !getId().equals(that.getId()) : that.getId() != null);

  }

  @Override
  public int hashCode() {
    return getId() != null ? getId().hashCode() : 0;
  }

}
