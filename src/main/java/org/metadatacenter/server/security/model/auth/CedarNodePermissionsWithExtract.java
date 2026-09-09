package org.metadatacenter.server.security.model.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionsRequest;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionUser;
import org.metadatacenter.server.security.model.user.CedarUserExtract;

import java.util.ArrayList;
import java.util.List;

/**
 * The grants recorded on one folder or artifact, with each user and group named rather than
 * referenced by identifier alone.
 *
 * <p>Two services answer with this payload: the resource server serves and replaces it on the
 * permissions routes, and the monitor server nests it in its diagnostic reports. Each described it
 * separately, and the two descriptions disagreed, so the description now sits on the type and both
 * documents take it from here. The OpenAPI annotations carry no runtime behavior.</p>
 */
@Schema(name = "ResourcePermissions",
    description = "The owner of a folder or artifact, together with the user and group grants "
        + "recorded directly on it. A role that reaches the resource through a folder above it is "
        + "not listed here.")
public class CedarNodePermissionsWithExtract {

  private CedarUserExtract owner;
  private final List<CedarNodeUserPermission> userPermissions;
  private final List<CedarNodeGroupPermission> groupPermissions;


  public CedarNodePermissionsWithExtract() {
    userPermissions = new ArrayList<>();
    groupPermissions = new ArrayList<>();
  }

  @Schema(name = "owner", requiredMode = Schema.RequiredMode.REQUIRED,
      description = "The user who owns the resource.")
  public CedarUserExtract getOwner() {
    return owner;
  }

  public void setOwner(CedarUserExtract owner) {
    this.owner = owner;
  }

  @Schema(name = "userPermissions", requiredMode = Schema.RequiredMode.REQUIRED,
      description = "Every role granted directly to a user.")
  public List<CedarNodeUserPermission> getUserPermissions() {
    return userPermissions;
  }

  public void addUserPermissions(CedarNodeUserPermission userPermission) {
    userPermissions.add(userPermission);
  }

  @Schema(name = "groupPermissions", requiredMode = Schema.RequiredMode.REQUIRED,
      description = "Every role granted directly to a group.")
  public List<CedarNodeGroupPermission> getGroupPermissions() {
    return groupPermissions;
  }

  public void addGroupPermissions(CedarNodeGroupPermission groupPermission) {
    groupPermissions.add(groupPermission);
  }

  public ResourcePermissionsRequest toRequest() {
    ResourcePermissionsRequest r = new ResourcePermissionsRequest();
    // copy owner
    ResourcePermissionUser newOwner = new ResourcePermissionUser();
    newOwner.setId(owner.getId());
    r.setOwner(newOwner);
    // copy users
    for (CedarNodeUserPermission up : userPermissions) {
      r.getUserPermissions().add(up.getAsUserIdPermissionPair());
    }
    // copy groups
    for (CedarNodeGroupPermission gp : groupPermissions) {
      r.getGroupPermissions().add(gp.getAsGroupIdPermissionPair());
    }
    return r;
  }
}
