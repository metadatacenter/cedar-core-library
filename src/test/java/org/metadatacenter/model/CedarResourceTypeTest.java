package org.metadatacenter.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * The import path localizes an artifact's {@code @id} under the prefix of the resource type its
 * {@code @type} names, and it descends only into children whose {@code @type} names one. A static field
 * is stored as a field, under the field prefix.
 */
class CedarResourceTypeTest {

  @Test
  void eachArtifactTypeIsFoundByItsAtType() {
    assertSame(CedarResourceType.FIELD, CedarResourceType.forAtType(CedarResourceType.AtType.FIELD));
    assertSame(CedarResourceType.ELEMENT, CedarResourceType.forAtType(CedarResourceType.AtType.ELEMENT));
    assertSame(CedarResourceType.TEMPLATE, CedarResourceType.forAtType(CedarResourceType.AtType.TEMPLATE));
  }

  @Test
  void aStaticFieldIsAField() {
    assertSame(CedarResourceType.FIELD, CedarResourceType.forAtType(CedarResourceType.AtType.STATIC_FIELD));
  }

  @Test
  void anAtTypeThatNamesNoArtifactFindsNoType() {
    assertNull(CedarResourceType.forAtType("https://schema.metadatacenter.org/core/TemplateInstance"));
    assertNull(CedarResourceType.forAtType(null));
  }
}
