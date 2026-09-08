package org.metadatacenter.server.security.model.permission.resource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import org.metadatacenter.model.CedarResourceType;

import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;

import static org.metadatacenter.server.security.model.permission.resource.ResourceCapability.*;

/**
 * A role granted to a user or a group on an artifact or a folder.
 *
 * <p>Jackson writes the lowercase {@code value} rather than the constant name, so the OpenAPI
 * enum is derived from {@link #getValue()} in every document that publishes this type.</p>
 */
@Schema(name = "ResourceRole", enumAsRef = true,
    description = "A role granted to a user or a group on an artifact or a folder. Each role "
        + "includes everything the weaker roles allow.")
public enum ResourceRole {
  VIEWER("viewer", 1, EnumSet.of(READ_RESOURCE, LIST_FOLDER_CONTENTS)),
  EDITOR("editor", 2, EnumSet.of(
      READ_RESOURCE, LIST_FOLDER_CONTENTS, UPDATE_RESOURCE, CREATE_IN_FOLDER, COPY_INTO_FOLDER, MOVE_INTO_FOLDER,
      DELETE_RESOURCE)),
  MANAGER("manager", 3, EnumSet.of(
      READ_RESOURCE, LIST_FOLDER_CONTENTS, UPDATE_RESOURCE, CREATE_IN_FOLDER, COPY_INTO_FOLDER, MOVE_INTO_FOLDER,
      DELETE_RESOURCE, MANAGE_GRANTS, MOVE_RESOURCE, MANAGE_OPENVIEW));

  private final String value;
  private final int precedence;
  private final Set<ResourceCapability> capabilities;

  ResourceRole(String value, int precedence, Set<ResourceCapability> capabilities) {
    this.value = value;
    this.precedence = precedence;
    this.capabilities = Set.copyOf(capabilities);
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  public boolean provides(CedarResourceType resourceType, ResourceCapability capability) {
    return capability != null && capability.appliesTo(resourceType) && capabilities.contains(capability);
  }

  public boolean includes(ResourceRole requiredRole) {
    return requiredRole != null && precedence >= requiredRole.precedence;
  }

  public static ResourceRole strongest(ResourceRole first, ResourceRole second) {
    if (first == null) {
      return second;
    }
    if (second == null) {
      return first;
    }
    return first.precedence >= second.precedence ? first : second;
  }

  @JsonCreator
  public static ResourceRole forValue(String value) {
    if (value == null) {
      return null;
    }
    return switch (value.toLowerCase(Locale.ROOT)) {
      case "viewer" -> VIEWER;
      case "editor" -> EDITOR;
      case "manager" -> MANAGER;
      default -> null;
    };
  }
}
