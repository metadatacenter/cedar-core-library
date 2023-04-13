package org.metadatacenter.rest.context;

public enum CedarParameterSource {

  EmptyBody("EmptyBody"),
  JsonBody("JsonBody"),
  QueryString("QueryString"),
  InPlace("InPlace");

  private final String value;

  CedarParameterSource(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
