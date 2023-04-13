package org.metadatacenter.id;

import org.metadatacenter.model.CedarResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public abstract class CedarResourceId {

  protected String id;

  private static final Logger log = LoggerFactory.getLogger(CedarResourceId.class);

  protected CedarResourceId() {
  }

  protected CedarResourceId(String id) {
    this.id = id;
  }

  public static CedarResourceId build(String id, CedarResourceType type) {
    switch (type) {
      case FOLDER:
        return CedarFolderId.build(id);
      case FIELD:
        return CedarFieldId.build(id);
      case ELEMENT:
        return CedarElementId.build(id);
      case TEMPLATE:
        return CedarTemplateId.build(id);
      case INSTANCE:
        return CedarTemplateInstanceId.build(id);
      case CATEGORY:
        return CedarCategoryId.build(id);
      case GROUP:
        return CedarGroupId.build(id);
      case USER:
        return CedarUserId.build(id);
    }
    log.error("Error creating CedarResourceId, unrecognized type:" + type);
    return null;
  }

  public String getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CedarResourceId)) {
      return false;
    }
    CedarResourceId that = (CedarResourceId) o;
    return Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}
