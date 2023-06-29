package org.metadatacenter.server.security.model.user;

public class CedarUserUIMetadataEditorV2 {
  private boolean showTemplateRenderingRepresentation;
  private boolean showInstanceDataCore;
  private boolean expandedInstanceDataCore;
  private boolean showMultiInstanceInfo;
  private boolean expandedMultiInstanceInfo;
  private boolean collapseStaticComponents;
  private boolean showStaticText;
  private boolean multiInstanceTabIndexable;
  private boolean showMultiInstancePagination;
  private String formFieldBackgroundColor;
  private String formFieldAppearance;

  public CedarUserUIMetadataEditorV2() {
    showTemplateRenderingRepresentation = false;
    showInstanceDataCore = false;
    expandedInstanceDataCore = false;
    showMultiInstanceInfo = false;
    expandedMultiInstanceInfo = false;
    collapseStaticComponents = true;
    showStaticText = true;
    multiInstanceTabIndexable = false;
    showMultiInstancePagination = false;
    formFieldBackgroundColor = "#F1F6F1";
    formFieldAppearance = "outline";
  }

  public boolean isShowTemplateRenderingRepresentation() {
    return showTemplateRenderingRepresentation;
  }

  public void setShowTemplateRenderingRepresentation(boolean showTemplateRenderingRepresentation) {
    this.showTemplateRenderingRepresentation = showTemplateRenderingRepresentation;
  }

  public boolean isShowInstanceDataCore() {
    return showInstanceDataCore;
  }

  public void setShowInstanceDataCore(boolean showInstanceDataCore) {
    this.showInstanceDataCore = showInstanceDataCore;
  }

  public boolean isExpandedInstanceDataCore() {
    return expandedInstanceDataCore;
  }

  public void setExpandedInstanceDataCore(boolean expandedInstanceDataCore) {
    this.expandedInstanceDataCore = expandedInstanceDataCore;
  }

  public boolean isShowMultiInstanceInfo() {
    return showMultiInstanceInfo;
  }

  public void setShowMultiInstanceInfo(boolean showMultiInstanceInfo) {
    this.showMultiInstanceInfo = showMultiInstanceInfo;
  }

  public boolean isExpandedMultiInstanceInfo() {
    return expandedMultiInstanceInfo;
  }

  public void setExpandedMultiInstanceInfo(boolean expandedMultiInstanceInfo) {
    this.expandedMultiInstanceInfo = expandedMultiInstanceInfo;
  }

  public boolean isCollapseStaticComponents() {
    return collapseStaticComponents;
  }

  public void setCollapseStaticComponents(boolean collapseStaticComponents) {
    this.collapseStaticComponents = collapseStaticComponents;
  }

  public boolean isShowStaticText() {
    return showStaticText;
  }

  public void setShowStaticText(boolean showStaticText) {
    this.showStaticText = showStaticText;
  }

  public boolean isMultiInstanceTabIndexable() {
    return multiInstanceTabIndexable;
  }

  public void setMultiInstanceTabIndexable(boolean multiInstanceTabIndexable) {
    this.multiInstanceTabIndexable = multiInstanceTabIndexable;
  }

  public boolean isShowMultiInstancePagination() {
    return showMultiInstancePagination;
  }

  public void setShowMultiInstancePagination(boolean showMultiInstancePagination) {
    this.showMultiInstancePagination = showMultiInstancePagination;
  }

  public String getFormFieldBackgroundColor() {
    return formFieldBackgroundColor;
  }

  public void setFormFieldBackgroundColor(String formFieldBackgroundColor) {
    this.formFieldBackgroundColor = formFieldBackgroundColor;
  }

  public String getFormFieldAppearance() {
    return formFieldAppearance;
  }

  public void setFormFieldAppearance(String formFieldAppearance) {
    this.formFieldAppearance = formFieldAppearance;
  }
}
