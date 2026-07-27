package org.metadatacenter.server.security.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.util.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

// Tolerate unknown properties (like CedarUser does) so adding/removing a UI preference in one
// service does not break deserialization of the user object in other services or from stored data.
@JsonIgnoreProperties(ignoreUnknown = true)
public class CedarUserUIPreferences {

  private static final Logger log = LoggerFactory.getLogger(CedarUserUIPreferences.class);

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

  // User's preferred date format, as a moment.js-style token (e.g. "MM/DD/YYYY"). Defaults non-null
  // so the value is always present and served up in the user profile.
  private String preferredDateFormat = "MM/DD/YYYY";

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
      if (deser.preferredDateFormat != null) {
        preferredDateFormat = deser.preferredDateFormat;
      }
    } catch (IOException e) {
      // The preferences keep their defaults, which is a survivable outcome; log it so a user
      // reporting that their settings reverted has something to point at.
      log.error("Could not deserialize the stored UI preferences; keeping the defaults", e);
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

  public String getPreferredDateFormat() {
    return preferredDateFormat;
  }

  public void setPreferredDateFormat(String preferredDateFormat) {
    this.preferredDateFormat = preferredDateFormat;
  }
}
