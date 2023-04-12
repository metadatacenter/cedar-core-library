package org.metadatacenter.server.security.model.user;

public class CedarUserUIFolderView {

  private String currentFolderId;

  private String sortBy;

  private SortDirection sortDirection;

  private ViewMode viewMode;

  public CedarUserUIFolderView() {
  }

  public String getCurrentFolderId() {
    return currentFolderId;
  }

  public void setCurrentFolderId(String currentFolderId) {
    this.currentFolderId = currentFolderId;
  }

  public String getSortBy() {
    return sortBy;
  }

  public void setSortBy(String sortBy) {
    this.sortBy = sortBy;
  }

  public SortDirection getSortDirection() {
    return sortDirection;
  }

  public void setSortDirection(SortDirection sortDirection) {
    this.sortDirection = sortDirection;
  }

  public ViewMode getViewMode() {
    return viewMode;
  }

  public void setViewMode(ViewMode viewMode) {
    this.viewMode = viewMode;
  }
}
