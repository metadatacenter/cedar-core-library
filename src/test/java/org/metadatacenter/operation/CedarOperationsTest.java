package org.metadatacenter.operation;

import org.junit.jupiter.api.Test;
import org.metadatacenter.rest.assertion.noun.CedarParameter;
import org.metadatacenter.rest.context.CedarParameterSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A refusal describes the refused operation in its {@code operation} object. For an operation on a
 * request parameter, the attribute value it reports is the text the caller sent.
 */
class CedarOperationsTest {

  @Test
  void aCreateNamesTheParameterValueTheCallerSent() {
    CedarParameter name = parameter("schema:name", "My group");

    assertEquals("My group",
        CedarOperations.create(Object.class, "schema:name", name).asJson().get("primaryIdAttributeValue").asText());
    assertEquals("My group",
        CedarOperations.lookup(Object.class, "schema:name", name).asJson().get("lookupAttributeValue").asText());
  }

  private static CedarParameter parameter(String parameterName, String value) {
    return new CedarParameter() {
      @Override
      public String stringValue() {
        return value;
      }

      @Override
      public boolean booleanValue() {
        return false;
      }

      @Override
      public String getName() {
        return parameterName;
      }

      @Override
      public CedarParameterSource getSource() {
        return null;
      }

      @Override
      public boolean isNull() {
        return false;
      }

      @Override
      public boolean isPresentAndNull() {
        return false;
      }

      @Override
      public boolean isMissing() {
        return false;
      }

      @Override
      public boolean isEmpty() {
        return false;
      }

      @Override
      public void trim() {
      }
    };
  }
}
