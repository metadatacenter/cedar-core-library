package org.metadatacenter.constant;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class CedarConstants {

  private CedarConstants() {
  }

  // JSON ID constants
  public static final String TEMP_ID_PREFIX = "tmp-";

  // Date formats
  public static final String xsdDateTimeFormatterString = "uuuu-MM-dd'T'HH:mm:ssZZZZZ";
  public static final DateTimeFormatter xsdDateTimeFormatter =
      DateTimeFormatter.ofPattern(xsdDateTimeFormatterString).withZone(ZoneId.systemDefault());
}
