package org.metadatacenter.server.security.model.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.metadatacenter.model.CedarResourceType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * How the Workspace lists a folder, a search or a shared view: which artifacts it shows, in what
 * order, and as a grid or a list. The legacy Workbench keeps its own choices in the folder view and
 * filter objects beside this one. The two read their type and version filters differently, so
 * neither writes the other's.
 */
public class CedarUserUIWorkspace {

  // The kinds of artifact listed. An empty list lists every kind. A kind this release does not know
  // is dropped, as an unknown value elsewhere in the preferences costs only itself.
  @JsonFormat(with = JsonFormat.Feature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)
  @JsonSetter(contentNulls = Nulls.SKIP)
  @ArraySchema(schema = @Schema(type = "string",
      allowableValues = {"folder", "field", "element", "template", "instance"}))
  private List<CedarResourceType> resourceTypes = new ArrayList<>();

  // The last-modified range, or null for any date. The bounds apply only to a custom range.
  private ResourceModifiedFilter modified;

  private LocalDate modifiedAfter;

  private LocalDate modifiedBefore;

  private ResourceVersionFilter version = ResourceVersionFilter.ALL;

  private ResourceSortField sortBy = ResourceSortField.NAME;

  private SortDirection sortDirection = SortDirection.ASC;

  private boolean foldersFirst;

  private ViewMode viewMode = ViewMode.GRID;

  public CedarUserUIWorkspace() {
  }

  public List<CedarResourceType> getResourceTypes() {
    return resourceTypes;
  }

  public void setResourceTypes(List<CedarResourceType> resourceTypes) {
    this.resourceTypes = resourceTypes;
  }

  public ResourceModifiedFilter getModified() {
    return modified;
  }

  public void setModified(ResourceModifiedFilter modified) {
    this.modified = modified;
  }

  public LocalDate getModifiedAfter() {
    return modifiedAfter;
  }

  public void setModifiedAfter(LocalDate modifiedAfter) {
    this.modifiedAfter = modifiedAfter;
  }

  public LocalDate getModifiedBefore() {
    return modifiedBefore;
  }

  public void setModifiedBefore(LocalDate modifiedBefore) {
    this.modifiedBefore = modifiedBefore;
  }

  public ResourceVersionFilter getVersion() {
    return version;
  }

  public void setVersion(ResourceVersionFilter version) {
    this.version = version;
  }

  public ResourceSortField getSortBy() {
    return sortBy;
  }

  public void setSortBy(ResourceSortField sortBy) {
    this.sortBy = sortBy;
  }

  public SortDirection getSortDirection() {
    return sortDirection;
  }

  public void setSortDirection(SortDirection sortDirection) {
    this.sortDirection = sortDirection;
  }

  public boolean isFoldersFirst() {
    return foldersFirst;
  }

  public void setFoldersFirst(boolean foldersFirst) {
    this.foldersFirst = foldersFirst;
  }

  public ViewMode getViewMode() {
    return viewMode;
  }

  public void setViewMode(ViewMode viewMode) {
    this.viewMode = viewMode;
  }
}
