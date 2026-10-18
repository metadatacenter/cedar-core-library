package org.metadatacenter.util.json;

import java.time.OffsetDateTime;

/** Compatibility entry for the shared, dependency-light model timestamp policy. */
public final class ProvenanceTimestamp {
  private ProvenanceTimestamp() {}
  public static OffsetDateTime parse(String value) {
    return org.metadatacenter.model.policy.ProvenanceTimestamp.parse(value);
  }
  public static String format(OffsetDateTime value) {
    return org.metadatacenter.model.policy.ProvenanceTimestamp.format(value);
  }
}
