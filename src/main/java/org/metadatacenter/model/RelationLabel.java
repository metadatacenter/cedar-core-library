package org.metadatacenter.model;

import org.metadatacenter.server.security.model.permission.category.CategoryRole;
import org.metadatacenter.server.security.model.permission.resource.ResourceRole;

public enum RelationLabel {

  OWNS(PlainLabels.OWNS, null, null),
  CONTAINS(PlainLabels.CONTAINS, null, null),
  MEMBEROF(PlainLabels.MEMBEROF, null, null),
  CANREAD(PlainLabels.CANREAD, ResourceRole.VIEWER, null),
  CANWRITE(PlainLabels.CANWRITE, ResourceRole.MANAGER, null),
  EDITOR_ROLE(PlainLabels.EDITOR_ROLE, ResourceRole.EDITOR, CategoryRole.EDITOR),
  VIEWER_ROLE(PlainLabels.VIEWER_ROLE, ResourceRole.VIEWER, CategoryRole.VIEWER),
  MANAGER_ROLE(PlainLabels.MANAGER_ROLE, ResourceRole.MANAGER, null),
  ADMINISTERS(PlainLabels.ADMINISTERS, null, null),
  PREVIOUSVERSION(PlainLabels.PREVIOUSVERSION, null, null),
  DERIVEDFROM(PlainLabels.DERIVEDFROM, null, null),
  //
  CONTAINSCATEGORY(PlainLabels.CONTAINSCATEGORY, null, null),
  CANATTACHCATEGORY(PlainLabels.CANATTACHCATEGORY, null, CategoryRole.CLASSIFIER),
  CANWRITECATEGORY(PlainLabels.CANWRITECATEGORY, null, CategoryRole.MANAGER),
  OWNSCATEGORY(PlainLabels.OWNSCATEGORY, null, null),
  CONTAINSARTIFACT(PlainLabels.CONTAINSARTIFACT, null, null),
  INCLUDES(PlainLabels.INCLUDES, null, null);

  public static class PlainLabels {
    public static final String OWNS = "OWNS";
    public static final String CONTAINS = "CONTAINS";
    public static final String MEMBEROF = "MEMBEROF";
    public static final String CANREAD = "CANREAD";
    public static final String CANWRITE = "CANWRITE";
    public static final String EDITOR_ROLE = "EDITOR_ROLE";
    public static final String VIEWER_ROLE = "VIEWER_ROLE";
    public static final String MANAGER_ROLE = "MANAGER_ROLE";
    public static final String ADMINISTERS = "ADMINISTERS";
    public static final String PREVIOUSVERSION = "PREVIOUSVERSION";
    public static final String DERIVEDFROM = "DERIVEDFROM";
    //
    public static final String CANATTACHCATEGORY = "CANATTACHCATEGORY";
    public static final String CONTAINSCATEGORY = "CONTAINSCATEGORY";
    public static final String CANWRITECATEGORY = "CANWRITECATEGORY";
    public static final String OWNSCATEGORY = "OWNSCATEGORY";
    public static final String CONTAINSARTIFACT = "CONTAINSARTIFACT";
    public static final String INCLUDES = "INCLUDES";
  }

  private final String value;
  private final ResourceRole resourceRole;
  private final CategoryRole categoryRole;

  RelationLabel(String value, ResourceRole resourceRole,
                CategoryRole categoryRole) {
    this.value = value;
    this.resourceRole = resourceRole;
    this.categoryRole = categoryRole;
  }

  public String getValue() {
    return value;
  }

  public CategoryRole getCategoryRole() {
    return categoryRole;
  }

  public ResourceRole getResourceRole() {
    return resourceRole;
  }

  public static RelationLabel forValue(String type) {
    for (RelationLabel t : values()) {
      if (t.getValue().equals(type)) {
        return t;
      }
    }
    return null;
  }

  /** Return the relationship written before the later legacy-name migration. */
  public static RelationLabel forResourceRole(ResourceRole role) {
    return switch (role) {
      case VIEWER -> CANREAD;
      case EDITOR -> EDITOR_ROLE;
      case MANAGER -> CANWRITE;
    };
  }

  public static RelationLabel forCategoryRole(CategoryRole role) {
    if (role != null) {
      for (RelationLabel t : values()) {
        if (role == t.getCategoryRole()) {
          return t;
        }
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return value;
  }
}
