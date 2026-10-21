package org.metadatacenter.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.metadatacenter.server.security.model.permission.category.CategoryRole;
import org.metadatacenter.server.security.model.permission.resource.ResourceRole;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Grants live in the graph as relationships, and the relationship's type is the only record of the
 * role. The workspace library reads a resource grant's role through these labels and writes a category
 * grant under the label its category role selects, so the names are stored data. A changed mapping
 * hides every existing grant of that role.
 */
class RelationLabelTest {

  /**
   * The grant writer stores CANREAD, EDITOR_ROLE and CANWRITE. The reader accepts VIEWER_ROLE and
   * MANAGER_ROLE as well, so either name of a role reads back as that role.
   */
  @ParameterizedTest
  @CsvSource({
      "CANREAD,VIEWER", "VIEWER_ROLE,VIEWER",
      "EDITOR_ROLE,EDITOR",
      "CANWRITE,MANAGER", "MANAGER_ROLE,MANAGER"})
  void eachStoredResourceGrantReadsBackAsItsRole(String storedRelation, ResourceRole role) {
    assertSame(role, RelationLabel.forValue(storedRelation).getResourceRole());
  }

  /** The resource ACL reader refuses a relation without a resource role rather than guessing one. */
  @ParameterizedTest
  @EnumSource(value = RelationLabel.class, mode = EnumSource.Mode.EXCLUDE,
      names = {"CANREAD", "VIEWER_ROLE", "EDITOR_ROLE", "CANWRITE", "MANAGER_ROLE"})
  void everyOtherRelationCarriesNoResourceRole(RelationLabel label) {
    assertNull(label.getResourceRole(), label.getValue());
  }

  /** These are the four relations the category ACL reader accepts. */
  @ParameterizedTest
  @CsvSource({
      "VIEWER,VIEWER_ROLE", "CLASSIFIER,CANATTACHCATEGORY",
      "EDITOR,EDITOR_ROLE", "MANAGER,CANWRITECATEGORY"})
  void eachCategoryRoleIsWrittenUnderTheRelationItIsReadBackFrom(CategoryRole role, String storedRelation) {
    RelationLabel written = RelationLabel.forCategoryRole(role);

    assertEquals(storedRelation, written.toString());
    assertSame(role, RelationLabel.forValue(storedRelation).getCategoryRole());
  }

  @Test
  void noCategoryRoleSelectsNoRelation() {
    assertNull(RelationLabel.forCategoryRole(null));
  }

  /** The Cypher builders splice {@code toString()} in as the relationship type. */
  @ParameterizedTest
  @EnumSource(RelationLabel.class)
  void everyLabelIsFoundByTheNameItWritesIntoAQuery(RelationLabel label) {
    assertSame(label, RelationLabel.forValue(label.toString()));
  }

  /** Relationship types are case-sensitive in the graph, so the lookup is too. */
  @Test
  void anUnknownRelationNameFindsNoLabel() {
    assertNull(RelationLabel.forValue("canread"));
    assertNull(RelationLabel.forValue("READS"));
    assertNull(RelationLabel.forValue(""));
    assertNull(RelationLabel.forValue(null));
  }
}
