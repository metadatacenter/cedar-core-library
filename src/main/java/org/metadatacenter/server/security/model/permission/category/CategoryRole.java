package org.metadatacenter.server.security.model.permission.category;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;

import static org.metadatacenter.server.security.model.permission.category.CategoryCapability.*;

/** A role granted to a user or group on a category. */
public enum CategoryRole {
  VIEWER("viewer", 1, EnumSet.of(READ_CATEGORY)),
  CLASSIFIER("classifier", 2, EnumSet.of(READ_CATEGORY, ATTACH_CATEGORY, DETACH_CATEGORY)),
  EDITOR("editor", 3, EnumSet.of(
      READ_CATEGORY, ATTACH_CATEGORY, DETACH_CATEGORY, UPDATE_CATEGORY, CREATE_CHILD_CATEGORY,
      DELETE_CATEGORY)),
  MANAGER("manager", 4, EnumSet.of(
      READ_CATEGORY, ATTACH_CATEGORY, DETACH_CATEGORY, UPDATE_CATEGORY, CREATE_CHILD_CATEGORY,
      DELETE_CATEGORY, MANAGE_GRANTS, MOVE_CATEGORY));

  private final String value;
  private final int precedence;
  private final Set<CategoryCapability> capabilities;

  CategoryRole(String value, int precedence, Set<CategoryCapability> capabilities) {
    this.value = value;
    this.precedence = precedence;
    this.capabilities = Set.copyOf(capabilities);
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  public boolean provides(CategoryCapability capability) {
    return capability != null && capabilities.contains(capability);
  }

  public boolean includes(CategoryRole requiredRole) {
    return requiredRole != null && precedence >= requiredRole.precedence;
  }

  public static CategoryRole strongest(CategoryRole first, CategoryRole second) {
    if (first == null) {
      return second;
    }
    if (second == null) {
      return first;
    }
    return first.precedence >= second.precedence ? first : second;
  }

  @JsonCreator
  public static CategoryRole forValue(String value) {
    if (value == null) {
      return null;
    }
    return switch (value.toLowerCase(Locale.ROOT)) {
      case "viewer" -> VIEWER;
      case "classifier" -> CLASSIFIER;
      case "editor" -> EDITOR;
      case "manager" -> MANAGER;
      default -> null;
    };
  }
}
