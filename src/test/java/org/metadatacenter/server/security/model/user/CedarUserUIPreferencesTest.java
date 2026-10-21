package org.metadatacenter.server.security.model.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.metadatacenter.util.json.JsonMapper;

/**
 * The graph stores a user's interface preferences as one JSON string, written by the strict mapper,
 * and Jackson hands that string to the single-argument constructor when it reads the user. The stored
 * spellings of the filter and view values are therefore data that earlier releases wrote.
 */
public class CedarUserUIPreferencesTest {

  private static CedarUserUIPreferences asStoredInTheGraph(String stored) throws Exception {
    return JsonMapper.TOLERANT_MAPPER.readValue(
        JsonMapper.STRICT_MAPPER.writeValueAsString(stored), CedarUserUIPreferences.class);
  }

  @Test
  public void aStoredRecordReadsBackWithEveryValueItHolds() throws Exception {
    CedarUserUIPreferences preferences = asStoredInTheGraph("{"
        + "\"folderView\":{\"currentFolderId\":\"f1\",\"sortBy\":\"name\",\"sortDirection\":\"desc\",\"viewMode\":\"list\"},"
        + "\"resourceTypeFilters\":{\"field\":false,\"element\":true,\"template\":true,\"instance\":false},"
        + "\"resourcePublicationStatusFilter\":{\"publicationStatus\":\"bibo:published\"},"
        + "\"resourceVersionFilter\":{\"version\":\"latest-by-status\"},"
        + "\"infoPanel\":{\"opened\":true,\"activeTab\":\"share\"},"
        + "\"useMetadataEditorV2\":true,\"stylesheet\":\"dark\",\"preferredDateFormat\":\"DD/MM/YYYY\"}");

    Assertions.assertEquals("f1", preferences.getFolderView().getCurrentFolderId());
    Assertions.assertEquals("name", preferences.getFolderView().getSortBy());
    Assertions.assertEquals(SortDirection.DESC, preferences.getFolderView().getSortDirection());
    Assertions.assertEquals(ViewMode.LIST, preferences.getFolderView().getViewMode());
    Assertions.assertTrue(preferences.getResourceTypeFilters().isElement());
    Assertions.assertFalse(preferences.getResourceTypeFilters().isInstance());
    Assertions.assertEquals(ResourcePublicationStatusFilter.PUBLISHED,
        preferences.getResourcePublicationStatusFilter().getPublicationStatus());
    Assertions.assertEquals(ResourceVersionFilter.LATEST_BY_STATUS, preferences.getResourceVersionFilter().getVersion());
    Assertions.assertTrue(preferences.getInfoPanel().isOpened());
    Assertions.assertEquals(CedarUserInfoPanelTab.SHARE, preferences.getInfoPanel().getActiveTab());
    Assertions.assertTrue(preferences.isUseMetadataEditorV2());
    Assertions.assertEquals("dark", preferences.getStylesheet());
    Assertions.assertEquals("DD/MM/YYYY", preferences.getPreferredDateFormat());
  }

  @Test
  public void whatTheGraphWriterStoresReadsBackUnchanged() throws Exception {
    CedarUserUIPreferences preferences = new CedarUserUIPreferences();
    preferences.getFolderView().setViewMode(ViewMode.GRID);
    preferences.getFolderView().setSortDirection(SortDirection.ASC);
    preferences.getResourceVersionFilter().setVersion(ResourceVersionFilter.ALL);
    preferences.getResourcePublicationStatusFilter().setPublicationStatus(ResourcePublicationStatusFilter.DRAFT);
    preferences.getInfoPanel().setActiveTab(CedarUserInfoPanelTab.VERSION);
    preferences.setStylesheet("contrast");
    String stored = JsonMapper.STRICT_MAPPER.writeValueAsString(preferences);

    CedarUserUIPreferences readBack = asStoredInTheGraph(stored);

    Assertions.assertEquals(stored, JsonMapper.STRICT_MAPPER.writeValueAsString(readBack));
  }

  @Test
  public void aRecordStoredBeforeTheDateFormatExistedReadsWithTheDefault() throws Exception {
    CedarUserUIPreferences older = asStoredInTheGraph("{\"stylesheet\":\"dark\"}");
    CedarUserUIPreferences cleared = asStoredInTheGraph("{\"preferredDateFormat\":null}");

    Assertions.assertEquals("MM/DD/YYYY", older.getPreferredDateFormat());
    Assertions.assertEquals("dark", older.getStylesheet());
    Assertions.assertEquals("MM/DD/YYYY", cleared.getPreferredDateFormat());
  }

  @Test
  public void aPropertyFromAnotherReleaseDoesNotCostTheUserTheirPreferences() throws Exception {
    CedarUserUIPreferences preferences = asStoredInTheGraph(
        "{\"stylesheet\":\"dark\",\"addedLater\":{\"x\":1},\"folderView\":{\"viewMode\":\"list\",\"addedLater\":true}}");

    Assertions.assertEquals("dark", preferences.getStylesheet());
    Assertions.assertEquals(ViewMode.LIST, preferences.getFolderView().getViewMode());
  }

  @Test
  public void anUnrecognizedFilterValueReadsAsNoFilterAndKeepsTheRest() throws Exception {
    CedarUserUIPreferences preferences = asStoredInTheGraph("{\"stylesheet\":\"dark\","
        + "\"resourceVersionFilter\":{\"version\":\"newest\"},"
        + "\"resourcePublicationStatusFilter\":{\"publicationStatus\":\"bibo:retracted\"},"
        + "\"infoPanel\":{\"opened\":true,\"activeTab\":\"history\"}}");

    Assertions.assertNull(preferences.getResourceVersionFilter().getVersion());
    Assertions.assertNull(preferences.getResourcePublicationStatusFilter().getPublicationStatus());
    Assertions.assertNull(preferences.getInfoPanel().getActiveTab());
    Assertions.assertTrue(preferences.getInfoPanel().isOpened());
    Assertions.assertEquals("dark", preferences.getStylesheet());
  }
}
