package org.metadatacenter.operation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.metadatacenter.util.json.JsonMapper;

public class CedarCreateWithIdOperation implements CedarOperationDescriptor {

  private final Class clazz;
  private final String primaryIdAttributeName;
  private final Object primaryIdAttributeValue;

  public CedarCreateWithIdOperation(Class clazz, String primaryIdAttributeName, Object primaryIdAttributeValue) {
    this.clazz = clazz;
    this.primaryIdAttributeName = primaryIdAttributeName;
    this.primaryIdAttributeValue = primaryIdAttributeValue;
  }

  public Class getClazz() {
    return clazz;
  }

  public String getPrimaryIdAttributeName() {
    return primaryIdAttributeName;
  }

  public Object getPrimaryIdAttributeValue() {
    return primaryIdAttributeValue;
  }

  @Override
  public JsonNode asJson() {
    ObjectNode objectNode = JsonMapper.MAPPER.createObjectNode();
    objectNode.put("type", "createWithId");
    objectNode.put("className", clazz.getName());
    objectNode.put("simpleClassName", clazz.getSimpleName());
    objectNode.put("primaryIdAttributeName", primaryIdAttributeName);
    objectNode.put("primaryIdAttributeValue", primaryIdAttributeValue == null ? null :
        primaryIdAttributeValue.toString());
    return objectNode;
  }
}
