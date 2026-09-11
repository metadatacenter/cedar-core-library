package org.metadatacenter.server.security.model.permission.resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.metadatacenter.id.CedarUserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Schema(name = "CedarUserIdReference",
    description = "A user named by identifier alone. A write uses it wherever a listing answers "
        + "with a fuller user reference, because only the identifier decides which user is meant.",
    additionalProperties = Schema.AdditionalPropertiesValue.TRUE)
public class ResourcePermissionUser {

  private String id;

  private static final Logger log = LoggerFactory.getLogger(ResourcePermissionUser.class);

  public ResourcePermissionUser() {
  }

  public ResourcePermissionUser(String id) {
    this.id = id;
  }

  @JsonProperty("@id")
  @Schema(name = "@id", requiredMode = Schema.RequiredMode.REQUIRED, format = "uri",
      description = "Identifier of the user.")
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

    ResourcePermissionUser that = (ResourcePermissionUser) o;

    return !(getId() != null ? !getId().equals(that.getId()) : that.getId() != null);

  }

  @Override
  public int hashCode() {
    return getId() != null ? getId().hashCode() : 0;
  }

  @JsonIgnore
  public CedarUserId getResourceIds() {
    return CedarUserId.build(getId());
  }
}
