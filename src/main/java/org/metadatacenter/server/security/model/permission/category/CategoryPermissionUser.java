package org.metadatacenter.server.security.model.permission.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.id.CedarUserId;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryPermissionUser {

  private String id;

  public CategoryPermissionUser() {
  }

  public CategoryPermissionUser(String id) {
    this.id = id;
  }

  @JsonProperty("@id")
  public String getId() {
    return id;
  }

  @JsonProperty("@id")
  public void setId(String id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CategoryPermissionUser that = (CategoryPermissionUser) o;

    return !(getId() != null ? !getId().equals(that.getId()) : that.getId() != null);

  }

  @Override
  public int hashCode() {
    return getId() != null ? getId().hashCode() : 0;
  }

  @JsonIgnore
  public CedarUserId getResourceId() {
    return CedarUserId.build(this.id);
  }

}
