package org.metadatacenter.server.security.model.user;

public class CedarUserUIInfoPanel {

  private boolean opened;
  private CedarUserInfoPanelTab activeTab;

  public CedarUserUIInfoPanel() {
  }

  public boolean isOpened() {
    return opened;
  }

  public void setOpened(boolean opened) {
    this.opened = opened;
  }

  public CedarUserInfoPanelTab getActiveTab() {
    return activeTab;
  }

  public void setActiveTab(CedarUserInfoPanelTab activeTab) {
    this.activeTab = activeTab;
  }
}
