package org.metadatacenter.model;

import org.metadatacenter.server.security.model.permission.category.CategoryPermission;
import org.metadatacenter.server.security.model.permission.resource.FilesystemResourcePermission;

public enum RelationLabel {

  OWNS(PlainLabels.OWNS, null, null),
  CONTAINS(PlainLabels.CONTAINS, null, null),
  MEMBEROF(PlainLabels.MEMBEROF, null, null),
  CANREAD(PlainLabels.CANREAD, FilesystemResourcePermission.READ, null),
  CANWRITE(PlainLabels.CANWRITE, FilesystemResourcePermission.WRITE, null),
  ADMINISTERS(PlainLabels.ADMINISTERS, null, null),
  PREVIOUSVERSION(PlainLabels.PREVIOUSVERSION, null, null),
  DERIVEDFROM(PlainLabels.DERIVEDFROM, null, null),
  //
  CONTAINSCATEGORY(PlainLabels.CONTAINSCATEGORY, null, null),
  CANATTACHCATEGORY(PlainLabels.CANATTACHCATEGORY, null, CategoryPermission.ATTACH),
  CANWRITECATEGORY(PlainLabels.CANWRITECATEGORY, null, CategoryPermission.WRITE),
  OWNSCATEGORY(PlainLabels.OWNSCATEGORY, null, null),
  CONTAINSARTIFACT(PlainLabels.CONTAINSARTIFACT, null, null);

  public static class PlainLabels {
    public static final String OWNS = "OWNS";
    public static final String CONTAINS = "CONTAINS";
    public static final String MEMBEROF = "MEMBEROF";
    public static final String CANREAD = "CANREAD";
    public static final String CANWRITE = "CANWRITE";
    public static final String ADMINISTERS = "ADMINISTERS";
    public static final String PREVIOUSVERSION = "PREVIOUSVERSION";
    public static final String DERIVEDFROM = "DERIVEDFROM";
    //
    public static final String CANATTACHCATEGORY = "CANATTACHCATEGORY";
    public static final String CONTAINSCATEGORY = "CONTAINSCATEGORY";
    public static final String CANWRITECATEGORY = "CANWRITECATEGORY";
    public static final String OWNSCATEGORY = "OWNSCATEGORY";
    public static final String CONTAINSARTIFACT = "CONTAINSARTIFACT";
  }

  private final String value;
  private final FilesystemResourcePermission filesystemResourcePermission;
  private final CategoryPermission categoryPermission;

  RelationLabel(String value, FilesystemResourcePermission filesystemResourcePermission, CategoryPermission categoryPermission) {
    this.value = value;
    this.filesystemResourcePermission = filesystemResourcePermission;
    this.categoryPermission = categoryPermission;
  }

  public String getValue() {
    return value;
  }

  public FilesystemResourcePermission getFilesystemResourcePermission() {
    return filesystemResourcePermission;
  }

  public CategoryPermission getCategoryPermission() {
    return categoryPermission;
  }

  public static RelationLabel forValue(String type) {
    for (RelationLabel t : values()) {
      if (t.getValue().equals(type)) {
        return t;
      }
    }
    return null;
  }

  public static RelationLabel forFilesystemResourcePermission(FilesystemResourcePermission permission) {
    if (permission != null) {
      for (RelationLabel t : values()) {
        if (permission.equals(t.getFilesystemResourcePermission())) {
          return t;
        }
      }
    }
    return null;
  }

  public static RelationLabel forCategoryPermission(CategoryPermission permission) {
    if (permission != null) {
      for (RelationLabel t : values()) {
        if (permission.equals(t.getCategoryPermission())) {
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
