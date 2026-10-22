package org.metadatacenter.server.security.model.auth;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.metadatacenter.model.CedarResourceType;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The permission the resource server demands before it creates or updates an artifact of each type,
 * and before it publishes or drafts a versioned one. The version commands refuse a type that selects
 * no versioned permission with a 400, so an instance, which has no versions, must select none.
 */
class CedarPermissionTest {

  private static final Set<CedarResourceType> VERSIONED = Set.of(
      CedarResourceType.FIELD, CedarResourceType.ELEMENT, CedarResourceType.TEMPLATE);

  @ParameterizedTest
  @EnumSource(CedarResourceType.class)
  void eachArtifactTypeSelectsItsOwnCreateAndUpdatePermissions(CedarResourceType type) {
    CedarPermission expectedCreate = switch (type) {
      case FIELD -> CedarPermission.TEMPLATE_FIELD_CREATE;
      case ELEMENT -> CedarPermission.TEMPLATE_ELEMENT_CREATE;
      case TEMPLATE -> CedarPermission.TEMPLATE_CREATE;
      case INSTANCE -> CedarPermission.TEMPLATE_INSTANCE_CREATE;
      default -> null;
    };
    CedarPermission expectedUpdate = switch (type) {
      case FIELD -> CedarPermission.TEMPLATE_FIELD_UPDATE;
      case ELEMENT -> CedarPermission.TEMPLATE_ELEMENT_UPDATE;
      case TEMPLATE -> CedarPermission.TEMPLATE_UPDATE;
      case INSTANCE -> CedarPermission.TEMPLATE_INSTANCE_UPDATE;
      default -> null;
    };

    assertEquals(expectedCreate, CedarPermission.getCreateForArtifactType(type), "create");
    assertEquals(expectedUpdate, CedarPermission.getUpdateForArtifactType(type), "update");
    assertEquals(VERSIONED.contains(type) ? expectedUpdate : null,
        CedarPermission.getUpdateForVersionedArtifactType(type), "versioned update");
  }
}
