package org.metadatacenter.server.neo4j.cypher;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * When the workspace library writes a node from a JSON document, it keeps the keys this lookup
 * recognizes and drops the rest. A stored property the lookup misses is silently not written, and an
 * on-the-fly property it recognizes, computed for one response, would be persisted into the graph.
 */
class NodePropertyTest {

  @ParameterizedTest
  @EnumSource(NodeProperty.class)
  void everyStoredPropertyIsRecognizedByItsName(NodeProperty property) {
    assertSame(property, NodeProperty.forValue(property.getValue()));
  }

  @Test
  void noOnTheFlyPropertyIsRecognized() throws IllegalAccessException {
    List<String> onTheFly = new ArrayList<>();
    for (Field field : NodeProperty.OnTheFly.class.getDeclaredFields()) {
      if (Modifier.isStatic(field.getModifiers()) && field.getType() == String.class) {
        onTheFly.add((String) field.get(null));
      }
    }

    assertFalse(onTheFly.isEmpty());
    for (String name : onTheFly) {
      assertNull(NodeProperty.forValue(name), name);
    }
  }

  @Test
  void anUnknownKeyIsNotRecognized() {
    assertNull(NodeProperty.forValue("name"), "the stored name is schema:name");
    assertNull(NodeProperty.forValue(""));
    assertNull(NodeProperty.forValue(null));
  }
}
