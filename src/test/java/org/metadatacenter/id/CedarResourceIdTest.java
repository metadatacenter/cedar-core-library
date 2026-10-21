package org.metadatacenter.id;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.metadatacenter.model.CedarResourceType;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * The servers turn an identifier string and a resource type into a typed identifier through these
 * builders, and the graph reader goes the other way, from an identifier class back to its type. A
 * builder that answers with the wrong class fails a cast far from the cause.
 */
class CedarResourceIdTest {

  private static final String ID = "https://repo.example.org/any/0f5a3b0e-6f1a-4d1b-9c2e-1a2b3c4d5e6f";

  private static final Set<CedarResourceType> FILESYSTEM = Set.of(CedarResourceType.FOLDER,
      CedarResourceType.FIELD, CedarResourceType.ELEMENT, CedarResourceType.TEMPLATE, CedarResourceType.INSTANCE);
  private static final Set<CedarResourceType> ARTIFACTS = Set.of(CedarResourceType.FIELD,
      CedarResourceType.ELEMENT, CedarResourceType.TEMPLATE, CedarResourceType.INSTANCE);
  private static final Set<CedarResourceType> SCHEMA_ARTIFACTS = Set.of(CedarResourceType.FIELD,
      CedarResourceType.ELEMENT, CedarResourceType.TEMPLATE);

  @ParameterizedTest
  @EnumSource(CedarResourceType.class)
  void eachTypeBuildsItsOwnIdentifierClassAndThatClassLeadsBackToTheType(CedarResourceType type) {
    Class<CedarResourceId> idClass = type.getResourceIdClass();
    CedarResourceId id = CedarResourceId.build(ID, type);

    if (idClass == null) {
      assertNull(id, "a type without an identifier class builds nothing");
      return;
    }
    assertEquals(idClass, id.getClass());
    assertEquals(ID, id.getId());
    assertSame(type, CedarResourceType.forResourceIdClass(idClass));
  }

  @ParameterizedTest
  @EnumSource(CedarResourceType.class)
  void eachNarrowerBuilderAnswersOnlyForTheTypesItHolds(CedarResourceType type) {
    assertBuilds(FILESYSTEM.contains(type), type, CedarFilesystemResourceId.build(ID, type));
    assertBuilds(ARTIFACTS.contains(type), type, CedarArtifactId.build(ID, type));
    assertBuilds(SCHEMA_ARTIFACTS.contains(type), type, CedarSchemaArtifactId.build(ID, type));
    assertBuilds(type == CedarResourceType.INSTANCE, type, CedarInstanceArtifactId.build(ID, type));
  }

  private static void assertBuilds(boolean expected, CedarResourceType type, CedarResourceId built) {
    if (expected) {
      assertNotNull(built, type.getValue());
      assertEquals(type.getResourceIdClass(), built.getClass(), type.getValue());
      assertEquals(ID, built.getId());
    } else {
      assertNull(built, type.getValue());
    }
  }

  /**
   * The bridge reads an artifact's type from its IRI before it mints a DOI, and the monitor accepts
   * its input as a complete identity only when the parts print back to exactly that input.
   */
  @ParameterizedTest
  @CsvSource({
      "https://repo.metadatacenter.org/templates/0f5a3b0e-6f1a-4d1b-9c2e-1a2b3c4d5e6f,https,repo.metadatacenter.org,TEMPLATE",
      "https://repo.metadatacenter.org/template-elements/0f5a3b0e-6f1a-4d1b-9c2e-1a2b3c4d5e6f,https,repo.metadatacenter.org,ELEMENT",
      "https://repo.metadatacenter.org/template-fields/0f5a3b0e-6f1a-4d1b-9c2e-1a2b3c4d5e6f,https,repo.metadatacenter.org,FIELD",
      "https://repo.metadatacenter.org/template-instances/0f5a3b0e-6f1a-4d1b-9c2e-1a2b3c4d5e6f,https,repo.metadatacenter.org,INSTANCE",
      "https://repo.metadatacenter.orgx/folders/0f5a3b0e-6f1a-4d1b-9c2e-1a2b3c4d5e6f,https,repo.metadatacenter.orgx,FOLDER",
      "http://metadatacenter.org/users/0f5a3b0e-6f1a-4d1b-9c2e-1a2b3c4d5e6f,http,metadatacenter.org,USER"})
  void aCedarIriSplitsIntoItsPartsAndPrintsBackUnchanged(String iri, String protocol, String serverName,
                                                         CedarResourceType type) {
    CedarFQResourceId id = CedarFQResourceId.build(iri);

    assertEquals(protocol, id.getProtocol());
    assertEquals(serverName, id.getServerName());
    assertSame(type, id.getType());
    assertEquals("0f5a3b0e-6f1a-4d1b-9c2e-1a2b3c4d5e6f", id.getUuid());
    assertEquals(iri, id.toString());
  }

  @Test
  void textThatIsNotACedarIriBuildsNoIdentifier() {
    assertNull(CedarFQResourceId.build("https://repo.metadatacenter.org/things/0f5a3b0e"),
        "a collection that names no resource type");
    assertNull(CedarFQResourceId.build("https://repo.metadatacenter.org/templates/0f5a3b0e/"),
        "a trailing slash shifts the segments, so the uuid is read as the collection");
    assertNull(CedarFQResourceId.build("0f5a3b0e-6f1a-4d1b-9c2e-1a2b3c4d5e6f"));
    assertNull(CedarFQResourceId.build(""));
  }
}
