package org.metadatacenter.error;

import java.util.ArrayList;
import java.util.List;

public class CedarErrorPackNormalizerRegistry {

  private static CedarErrorPackNormalizerRegistry instance;
  private List<CedarErrorPackNormalizer> normalizers;


  private static CedarErrorPackNormalizerRegistry buildInstance() {
    CedarErrorPackNormalizerRegistry registry = new CedarErrorPackNormalizerRegistry();
    registry.normalizers = new ArrayList<>();
    return registry;
  }

  public static CedarErrorPackNormalizerRegistry getInstance() {
    if (instance == null) {
      instance = buildInstance();
    }
    return instance;
  }

  public List<CedarErrorPackNormalizer> getNormalizers() {
    return normalizers;
  }
}
