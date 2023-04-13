package org.metadatacenter.server.security.model.auth;

import org.metadatacenter.error.CedarErrorKey;

public class CurrentUserResourcePermissions {
  private boolean canRead;
  private boolean canWrite;
  private boolean canDelete;
  private boolean canShare;
  private boolean canChangeOwner;
  private boolean canCreateDraft;
  private boolean canPublish;
  private boolean canSubmit;
  private boolean canPopulate;
  private boolean canCopy;
  private boolean canMakeOpen;
  private boolean canMakeNotOpen;

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

  public boolean isCanCreateDraft() {
    return canCreateDraft;
  }

  public void setCanCreateDraft(boolean canCreateDraft) {
    this.canCreateDraft = canCreateDraft;
  }

  public boolean isCanPublish() {
    return canPublish;
  }

  public void setCanPublish(boolean canPublish) {
    this.canPublish = canPublish;
  }

  public boolean isCanSubmit() {
    return canSubmit;
  }

  public void setCanSubmit(boolean canSubmit) {
    this.canSubmit = canSubmit;
  }

  public boolean isCanPopulate() {
    return canPopulate;
  }

  public void setCanPopulate(boolean canPopulate) {
    this.canPopulate = canPopulate;
  }

  public boolean isCanCopy() {
    return canCopy;
  }

  public void setCanCopy(boolean canCopy) {
    this.canCopy = canCopy;
  }

  public boolean isCanMakeOpen() {
    return canMakeOpen;
  }

  public void setCanMakeOpen(boolean canMakeOpen) {
    this.canMakeOpen = canMakeOpen;
  }

  public boolean isCanMakeNotOpen() {
    return canMakeNotOpen;
  }

  public void setCanMakeNotOpen(boolean canMakeNotOpen) {
    this.canMakeNotOpen = canMakeNotOpen;
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
