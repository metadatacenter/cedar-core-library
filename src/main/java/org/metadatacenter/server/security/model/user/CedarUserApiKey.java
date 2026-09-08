package org.metadatacenter.server.security.model.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.metadatacenter.constant.CedarConstants;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.util.HexFormat;

/**
 * A stored API key. User-record readers select the tolerant mapper so a field that a later release
 * adds or removes does not make the stored key set unreadable.
 */
public class CedarUserApiKey {
  private String id;
  private String key;
  private String serviceName;
  private String description;
  /**
   * Declared on the field so that every mapper, Dropwizard's included, writes CEDAR's {@code xsd:dateTime}
   * shape. Dropwizard's mapper keeps Jackson's default of writing dates as timestamps, which would turn
   * this into a decimal epoch second that no client expects.
   */
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CedarConstants.xsdDateTimeFormatterString)
  private OffsetDateTime creationDate;
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

  public OffsetDateTime getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(OffsetDateTime creationDate) {
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
