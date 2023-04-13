package org.metadatacenter.server.security.model.auth;

import java.util.ArrayList;
import java.util.List;

public class CedarResourceBatchAttachCategoryRequest {

  private String artifactId;
  private List<String> categoryIds;

  public CedarResourceBatchAttachCategoryRequest() {
    categoryIds = new ArrayList<>();
  }

  public String getArtifactId() {
    return artifactId;
  }

  public void setArtifactId(String artifactId) {
    this.artifactId = artifactId;
  }

  public List<String> getCategoryIds() {
    return categoryIds;
  }

  public void setCategoryIds(List<String> categoryIds) {
    this.categoryIds = categoryIds;
  }

}
