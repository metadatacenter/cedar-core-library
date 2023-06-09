package org.metadatacenter.server.security.model.user;

import org.metadatacenter.util.json.JsonMapper;

import java.io.IOException;

public class CedarUserUIPreferences {

  private CedarUserUIFolderView folderView;

  private CedarUserUIResourceTypeFilters resourceTypeFilters;

  private CedarUserUIResourcePublicationStatusFilter resourcePublicationStatusFilter;

  private CedarUserUIResourceVersionFilter resourceVersionFilter;

  private CedarUserUIInfoPanel infoPanel;

  private CedarUserUITemplateEditor templateEditor;

  private CedarUserUIMetadataEditor metadataEditor;

  private CedarUserUIMetadataEditorV2 metadataEditorV2;

  private boolean useMetadataEditorV2;

  private String stylesheet;

  public CedarUserUIPreferences() {
    folderView = new CedarUserUIFolderView();
    resourceTypeFilters = new CedarUserUIResourceTypeFilters();
    resourcePublicationStatusFilter = new CedarUserUIResourcePublicationStatusFilter();
    resourceVersionFilter = new CedarUserUIResourceVersionFilter();
    infoPanel = new CedarUserUIInfoPanel();
    templateEditor = new CedarUserUITemplateEditor();
    metadataEditor = new CedarUserUIMetadataEditor();
    metadataEditorV2 = new CedarUserUIMetadataEditorV2();
    useMetadataEditorV2 = false;
  }

  public CedarUserUIPreferences(String jsonSource) {
    try {
      CedarUserUIPreferences deser = JsonMapper.MAPPER.readValue(jsonSource, CedarUserUIPreferences.class);
      folderView = deser.folderView;
      resourceTypeFilters = deser.resourceTypeFilters;
      resourcePublicationStatusFilter = deser.resourcePublicationStatusFilter;
      resourceVersionFilter = deser.resourceVersionFilter;
      infoPanel = deser.infoPanel;
      templateEditor = deser.templateEditor;
      metadataEditor = deser.metadataEditor;
      metadataEditorV2 = deser.metadataEditorV2;
      useMetadataEditorV2 = deser.useMetadataEditorV2;
      stylesheet = deser.stylesheet;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public CedarUserUIFolderView getFolderView() {
    return folderView;
  }

  public void setFolderView(CedarUserUIFolderView folderView) {
    this.folderView = folderView;
  }

  public CedarUserUIResourceTypeFilters getResourceTypeFilters() {
    return resourceTypeFilters;
  }

  public void setResourceTypeFilters(CedarUserUIResourceTypeFilters resourceTypeFilters) {
    this.resourceTypeFilters = resourceTypeFilters;
  }

  public CedarUserUIResourcePublicationStatusFilter getResourcePublicationStatusFilter() {
    return resourcePublicationStatusFilter;
  }

  public void setResourcePublicationStatusFilter(CedarUserUIResourcePublicationStatusFilter resourcePublicationStatusFilter) {
    this.resourcePublicationStatusFilter = resourcePublicationStatusFilter;
  }

  public CedarUserUIResourceVersionFilter getResourceVersionFilter() {
    return resourceVersionFilter;
  }

  public void setResourceVersionFilter(CedarUserUIResourceVersionFilter resourceVersionFilter) {
    this.resourceVersionFilter = resourceVersionFilter;
  }

  public CedarUserUIInfoPanel getInfoPanel() {
    return infoPanel;
  }

  public void setInfoPanel(CedarUserUIInfoPanel infoPanel) {
    this.infoPanel = infoPanel;
  }

  public CedarUserUITemplateEditor getTemplateEditor() {
    return templateEditor;
  }

  public void setTemplateEditor(CedarUserUITemplateEditor templateEditor) {
    this.templateEditor = templateEditor;
  }

  public CedarUserUIMetadataEditor getMetadataEditor() {
    return metadataEditor;
  }

  public void setMetadataEditor(CedarUserUIMetadataEditor metadataEditor) {
    this.metadataEditor = metadataEditor;
  }

  public String getStylesheet() {
    return stylesheet;
  }

  public void setStylesheet(String stylesheet) {
    this.stylesheet = stylesheet;
  }

  public CedarUserUIMetadataEditorV2 getMetadataEditorV2() {
    return metadataEditorV2;
  }

  public void setMetadataEditorV2(CedarUserUIMetadataEditorV2 metadataEditorV2) {
    this.metadataEditorV2 = metadataEditorV2;
  }

  public boolean isUseMetadataEditorV2() {
    return useMetadataEditorV2;
  }

  public void setUseMetadataEditorV2(boolean useMetadataEditorV2) {
    this.useMetadataEditorV2 = useMetadataEditorV2;
  }
}
