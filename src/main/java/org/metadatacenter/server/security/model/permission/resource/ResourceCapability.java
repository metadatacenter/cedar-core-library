package org.metadatacenter.server.security.model.permission.resource;

import com.fasterxml.jackson.annotation.JsonValue;

import org.metadatacenter.model.CedarResourceType;

import java.util.EnumSet;
import java.util.Set;

import static org.metadatacenter.model.CedarResourceType.*;

/**
 * One operation governed by the artifact-and-folder permission model.
 *
 * <p>Capabilities are the vocabulary used by authorization checks. Roles are bundles of these
 * capabilities; ownership adds {@link #TRANSFER_OWNERSHIP} separately.</p>
 */
public enum ResourceCapability {
  READ_RESOURCE("readResource", FOLDER, FIELD, ELEMENT, TEMPLATE, INSTANCE),
  LIST_FOLDER_CONTENTS("listFolderContents", FOLDER),
  UPDATE_RESOURCE("updateResource", FOLDER, FIELD, ELEMENT, TEMPLATE, INSTANCE),
  CREATE_IN_FOLDER("createInFolder", FOLDER),
  COPY_INTO_FOLDER("copyIntoFolder", FOLDER),
  MOVE_INTO_FOLDER("moveIntoFolder", FOLDER),
  DELETE_RESOURCE("deleteResource", FOLDER, FIELD, ELEMENT, TEMPLATE, INSTANCE),
  MANAGE_GRANTS("manageGrants", FOLDER, FIELD, ELEMENT, TEMPLATE, INSTANCE),
  MOVE_RESOURCE("moveResource", FOLDER, FIELD, ELEMENT, TEMPLATE, INSTANCE),
  MANAGE_OPENVIEW("manageOpenView", FOLDER, FIELD, ELEMENT, TEMPLATE, INSTANCE),
  TRANSFER_OWNERSHIP("transferOwnership", FOLDER, FIELD, ELEMENT, TEMPLATE, INSTANCE);

  private final String value;
  private final Set<CedarResourceType> supportedResourceTypes;

  ResourceCapability(String value, CedarResourceType firstType, CedarResourceType... additionalTypes) {
    this.value = value;
    EnumSet<CedarResourceType> types = EnumSet.of(firstType, additionalTypes);
    this.supportedResourceTypes = Set.copyOf(types);
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  public boolean appliesTo(CedarResourceType resourceType) {
    return supportedResourceTypes.contains(resourceType);
  }

  public Set<CedarResourceType> getSupportedResourceTypes() {
    return supportedResourceTypes;
  }
}
