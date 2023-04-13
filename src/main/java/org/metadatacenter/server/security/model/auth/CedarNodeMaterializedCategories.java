package org.metadatacenter.server.security.model.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

public class CedarNodeMaterializedCategories {

  private final String id;
  private final Set<String> categoryIds;


  public CedarNodeMaterializedCategories(String id) {
    this.id = id;
    categoryIds = new HashSet<>();
  }

  @JsonProperty("@id")
  public String getId() {
    return id;
  }

  public Set<String> getCategoryIds() {
    return categoryIds;
  }

  public void addCategory(String id) {
    this.categoryIds.add(id);
  }

}
