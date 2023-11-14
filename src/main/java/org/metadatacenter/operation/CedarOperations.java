package org.metadatacenter.operation;

import org.metadatacenter.id.CedarResourceId;
import org.metadatacenter.rest.assertion.noun.CedarParameter;

public final class CedarOperations {

  private CedarOperations() {
  }

  public static CedarLookupOperation lookup(Class clazz, String lookupAttributeName, CedarParameter lookupAttributeValue) {
    return new CedarLookupOperation(clazz, lookupAttributeName, lookupAttributeValue.stringValue());
  }

  public static CedarLookupOperation lookup(Class clazz, String lookupAttributeName, CedarResourceId lookupId) {
    return new CedarLookupOperation(clazz, lookupAttributeName, lookupId.getId());
  }

  public static CedarLookupOperation lookup(Class clazz, String lookupAttributeName, String lookupAttributeValue) {
    return new CedarLookupOperation(clazz, lookupAttributeName, lookupAttributeValue);
  }

  public static CedarCreateOperation create(Class clazz, String primaryIdAttributeName, CedarParameter primaryIdAttributeValue) {
    return new CedarCreateOperation(clazz, primaryIdAttributeName, primaryIdAttributeValue);
  }

  public static CedarCreateOperation create(Class clazz, String primaryIdAttributeName, String primaryIdAttributeValue) {
    return new CedarCreateOperation(clazz, primaryIdAttributeName, primaryIdAttributeValue);
  }

  public static CedarCreateWithIdOperation createWithId(Class clazz, String primaryIdAttributeName, String primaryIdAttributeValue) {
    return new CedarCreateWithIdOperation(clazz, primaryIdAttributeName, primaryIdAttributeValue);
  }

  public static CedarUpdateOperation update(Class clazz, String lookupAttributeName, String lookupAttributeValue) {
    return new CedarUpdateOperation(clazz, lookupAttributeName, lookupAttributeValue);
  }

  public static CedarDeleteOperation delete(Class clazz, String lookupAttributeName, String lookupAttributeValue) {
    return new CedarDeleteOperation(clazz, lookupAttributeName, lookupAttributeValue);
  }

  public static CedarListOperation list(Class clazz, String lookupAttributeName, String lookupAttributeValue) {
    return new CedarListOperation(clazz, lookupAttributeName, lookupAttributeValue);
  }
}

