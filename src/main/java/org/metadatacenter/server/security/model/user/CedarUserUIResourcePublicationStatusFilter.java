package org.metadatacenter.server.security.model.user;

public class CedarUserUIResourcePublicationStatusFilter {

  private ResourcePublicationStatusFilter publicationStatus;

  public CedarUserUIResourcePublicationStatusFilter() {
  }

  public ResourcePublicationStatusFilter getPublicationStatus() {
    return publicationStatus;
  }

  public void setPublicationStatus(ResourcePublicationStatusFilter publicationStatus) {
    this.publicationStatus = publicationStatus;
  }
}
