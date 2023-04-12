package org.metadatacenter.server.security.model.permission.resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.id.CedarGroupId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourcePermissionGroup {

  private String id;

  private static final Logger log = LoggerFactory.getLogger(ResourcePermissionGroup.class);

  public ResourcePermissionGroup() {
  }

  public ResourcePermissionGroup(String id) {
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

    ResourcePermissionGroup that = (ResourcePermissionGroup) o;

    return !(getId() != null ? !getId().equals(that.getId()) : that.getId() != null);

  }

  @Override
  public int hashCode() {
    return getId() != null ? getId().hashCode() : 0;
  }

  @JsonIgnore
  public CedarGroupId getResourceId() {
    return CedarGroupId.build(getId());
  }
}
