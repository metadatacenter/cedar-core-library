package org.metadatacenter.server.security.model.auth;

import org.metadatacenter.error.CedarErrorKey;

public class CurrentUserCategoryPermissions {
  private boolean canRead;
  private boolean canWrite;
  private boolean canDelete;
  private boolean canShare;
  private boolean canChangeOwner;
  private boolean canAttach;
  private boolean canDetach;

  private CedarErrorKey createDraftErrorKey;
  private CedarErrorKey publishErrorKey;

  public boolean isCanRead() {
    return canRead;
  }

  public void setCanRead(boolean canRead) {
    this.canRead = canRead;
  }

  public boolean isCanWrite() {
    return canWrite;
  }

  public void setCanWrite(boolean canWrite) {
    this.canWrite = canWrite;
  }

  public boolean isCanDelete() {
    return canDelete;
  }

  public void setCanDelete(boolean canDelete) {
    this.canDelete = canDelete;
  }

  public boolean isCanShare() {
    return canShare;
  }

  public void setCanShare(boolean canShare) {
    this.canShare = canShare;
  }

  public boolean isCanChangeOwner() {
    return canChangeOwner;
  }

  public void setCanChangeOwner(boolean canChangeOwner) {
    this.canChangeOwner = canChangeOwner;
  }

  public boolean isCanAttach() {
    return canAttach;
  }

  public void setCanAttach(boolean canAttach) {
    this.canAttach = canAttach;
  }

  public boolean isCanDetach() {
    return canDetach;
  }

  public void setCanDetach(boolean canDetach) {
    this.canDetach = canDetach;
  }

  public CedarErrorKey getCreateDraftErrorKey() {
    return createDraftErrorKey;
  }

  public void setCreateDraftErrorKey(CedarErrorKey createDraftErrorKey) {
    this.createDraftErrorKey = createDraftErrorKey;
  }

  public CedarErrorKey getPublishErrorKey() {
    return publishErrorKey;
  }

  public void setPublishErrorKey(CedarErrorKey publishErrorKey) {
    this.publishErrorKey = publishErrorKey;
  }
}
