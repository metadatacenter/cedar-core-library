package org.metadatacenter.server.security.model.user;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;

public class CedarUserApiKey {
  private String id;
  private String key;
  private String serviceName;
  private String description;
  private LocalDateTime creationDate;
  private boolean enabled;

  public CedarUserApiKey() {
  }

  /**
   * A stable, non-secret identifier used by API-key management routes.
   *
   * <p>Keys stored before identifiers were introduced acquire a deterministic identifier from the
   * existing 256-bit random secret. This keeps the identifier stable across reads before the user is
   * next written, without exposing the secret itself. Newly issued keys receive an independent UUID
   * before they are stored.</p>
   */
  public String getId() {
    if (id == null && key != null) {
      id = legacyIdForKey(key);
    }
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDateTime creationDate) {
    this.creationDate = creationDate;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  private static String legacyIdForKey(String key) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(("cedar-api-key:" + key).getBytes(StandardCharsets.UTF_8));
      return "legacy-" + HexFormat.of().formatHex(hash);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("SHA-256 is unavailable", e);
    }
  }
}
