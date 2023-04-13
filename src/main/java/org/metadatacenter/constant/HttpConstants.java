package org.metadatacenter.constant;

import javax.ws.rs.core.Response;

public final class HttpConstants {


  private HttpConstants() {
  }

  // HTTP headers
  public static final String HTTP_HEADER_LINK = "Link";
  public static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
  public static final String HTTP_HEADER_AUTHORIZATION = "Authorization";
  public static final String HTTP_HEADER_ACCEPT = "Accept";

  // HTTP header prefix
  public static final String HTTP_AUTH_HEADER_BEARER_PREFIX = "Bearer ";
  public static final String HTTP_AUTH_HEADER_APIKEY_PREFIX = "apiKey ";

  // HTTP Link header types
  public static final String HEADER_LINK_TYPE_FIRST = "first";
  public static final String HEADER_LINK_TYPE_LAST = "last";
  public static final String HEADER_LINK_TYPE_PREV = "prev";
  public static final String HEADER_LINK_TYPE_NEXT = "next";

  // Content Types
  public static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";
  public static final String CONTENT_TYPE_APPLICATION_MERGE_PATCH_JSON = "application/merge-patch+json";

  // StatusCodes

  public static final int OK = Response.Status.OK.getStatusCode();
  public static final int FORBIDDEN = Response.Status.FORBIDDEN.getStatusCode();
  public static final int BAD_REQUEST =  Response.Status.BAD_REQUEST.getStatusCode();
  public static final int CREATED =  Response.Status.CREATED.getStatusCode();
  public static final int METHOD_NOT_ALLOWED = Response.Status.METHOD_NOT_ALLOWED.getStatusCode();
  public static final int NOT_FOUND = Response.Status.NOT_FOUND.getStatusCode();
  public static final int NO_CONTENT = Response.Status.NO_CONTENT.getStatusCode();

}
