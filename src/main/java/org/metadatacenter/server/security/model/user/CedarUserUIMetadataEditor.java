package org.metadatacenter.server.security.model.user;

public class CedarUserUIMetadataEditor {

  private boolean templateJsonViewer;

  private boolean metadataJsonViewer;

  public CedarUserUIMetadataEditor() {
  }

  public boolean isTemplateJsonViewer() {
    return templateJsonViewer;
  }

  public void setTemplateJsonViewer(boolean templateJsonViewer) {
    this.templateJsonViewer = templateJsonViewer;
  }

  public boolean isMetadataJsonViewer() {
    return metadataJsonViewer;
  }

  public void setMetadataJsonViewer(boolean metadataJsonViewer) {
    this.metadataJsonViewer = metadataJsonViewer;
  }
}
