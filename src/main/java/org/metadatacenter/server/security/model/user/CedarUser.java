package org.metadatacenter.server.security.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.id.CedarUserId;
import org.metadatacenter.server.security.model.auth.CedarPermission;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.metadatacenter.constant.HttpConstants.HTTP_AUTH_HEADER_APIKEY_PREFIX;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CedarUser implements CedarUserRepresentation {

  private String id;

  private String firstName;

  private String lastName;

  private String email;

  private String homeFolderId;

  private List<CedarUserApiKey> apiKeys;

  private List<CedarUserRole> roles;

  private List<String> permissions;

  @JsonIgnore
  private Set<String> permissionSet;

  private CedarUserUIPreferences uiPreferences;

  private CedarUserAuthSource authSource;

  public CedarUser() {
    this.apiKeys = new ArrayList<>();
    this.roles = new ArrayList<>();
    this.permissions = new ArrayList<>();
    this.permissionSet = new HashSet<>();
    this.uiPreferences = new CedarUserUIPreferences();
  }

  @JsonProperty("@id")
  public String getId() {
    return id;
  }

  @JsonProperty("@id")
  public void setId(String id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getHomeFolderId() {
    return homeFolderId;
  }

  public void setHomeFolderId(String homeFolderId) {
    this.homeFolderId = homeFolderId;
  }

  public List<CedarUserApiKey> getApiKeys() {
    return apiKeys;
  }

  public void setApiKeys(List<CedarUserApiKey> apiKeys) {
    this.apiKeys = apiKeys;
  }

  public List<CedarUserRole> getRoles() {
    return roles;
  }

  public void setRoles(List<CedarUserRole> roles) {
    this.roles = roles;
  }

  public List<String> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<String> permissions) {
    this.permissions = permissions;
    this.permissionSet = new HashSet<>();
    this.permissionSet.addAll(permissions);
  }

  public CedarUserUIPreferences getUiPreferences() {
    return uiPreferences;
  }

  public void setUiPreferences(CedarUserUIPreferences uiPreferences) {
    this.uiPreferences = uiPreferences;
  }

  @JsonIgnore
  public CedarUserAuthSource getAuthSource() {
    return authSource;
  }

  public void setAuthSource(CedarUserAuthSource authSource) {
    this.authSource = authSource;
  }

  @JsonIgnore
  public String getFirstActiveApiKey() {
    if (apiKeys != null && !apiKeys.isEmpty()) {
      for (CedarUserApiKey k : apiKeys) {
        if (k.isEnabled()) {
          return k.getKey();
        }
      }
    }
    return null;
  }

  @JsonIgnore
  public String getFirstApiKeyAuthHeader() {
    String apiKey = this.getFirstActiveApiKey();
    if (apiKey != null) {
      return HTTP_AUTH_HEADER_APIKEY_PREFIX + apiKey;
    } else {
      return null;
    }
  }

  public boolean has(CedarPermission permission) {
    return permission != null && permissionSet != null && permissionSet.contains(permission.getPermissionName());
  }

  @JsonIgnore
  @Override
  public CedarUserId getResourceId() {
    return CedarUserId.build(this.id);
  }
}
