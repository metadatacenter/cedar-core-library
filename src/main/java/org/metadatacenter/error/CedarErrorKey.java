package org.metadatacenter.error;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CedarErrorKey {

  NONE(null),

  TEMPLATE_ELEMENT_NOT_CREATED("templateElementNotCreated"),
  TEMPLATE_ELEMENT_NOT_FOUND("templateElementNotFound"),
  TEMPLATE_ELEMENT_NOT_DELETED("templateElementNotDeleted"),
  TEMPLATE_ELEMENT_NOT_UPDATED("templateElementNotUpdated"),
  TEMPLATE_ELEMENTS_NOT_LISTED("templateElementsNotListed"),

  TEMPLATE_FIELD_NOT_CREATED("templateFieldNotCreated"),
  TEMPLATE_FIELD_NOT_FOUND("templateFieldNotFound"),
  TEMPLATE_FIELD_NOT_DELETED("templateFieldNotDeleted"),
  TEMPLATE_FIELD_NOT_UPDATED("templateFieldNotUpdated"),
  TEMPLATE_FIELDS_NOT_LISTED("templateFieldsNotListed"),

  TEMPLATE_NOT_CREATED("templateNotCreated"),
  TEMPLATE_NOT_FOUND("templateNotFound"),
  TEMPLATE_NOT_DELETED("templateNotDeleted"),
  TEMPLATE_NOT_UPDATED("templateNotUpdated"),
  TEMPLATES_NOT_LISTED("templatesNotListed"),

  TEMPLATE_INSTANCE_NOT_CREATED("templateInstanceNotCreated"),
  TEMPLATE_INSTANCE_NOT_FOUND("templateInstanceNotFound"),
  TEMPLATE_INSTANCE_NOT_DELETED("templateInstanceNotDeleted"),
  TEMPLATE_INSTANCE_NOT_UPDATED("templateInstanceNotUpdated"),
  TEMPLATE_INSTANCES_NOT_LISTED("templateInstancesNotListed"),

  NO_READ_ACCESS_TO_RESOURCE("noReadAccessToResource"),
  NO_WRITE_ACCESS_TO_RESOURCE("noWriteAccessToResource"),

  NO_READ_ACCESS_TO_FOLDER("noReadAccessToFolder"),
  NO_WRITE_ACCESS_TO_FOLDER("noWriteAccessToFolder"),

  NO_READ_ACCESS_TO_TEMPLATE("noReadAccessToTemplate"),
  NO_WRITE_ACCESS_TO_TEMPLATE("noWriteAccessToTemplate"),

  NO_READ_ACCESS_TO_TEMPLATE_ELEMENT("noReadAccessToTemplateElement"),
  NO_WRITE_ACCESS_TO_TEMPLATE_ELEMENT("noWriteAccessToTemplateElement"),

  NO_READ_ACCESS_TO_TEMPLATE_FIELD("noReadAccessToTemplateField"),
  NO_WRITE_ACCESS_TO_TEMPLATE_FIELD("noWriteAccessToTemplateField"),

  NO_READ_ACCESS_TO_TEMPLATE_INSTANCE("noReadAccessToTemplateInstance"),
  NO_WRITE_ACCESS_TO_TEMPLATE_INSTANCE("noWriteAccessToTemplateInstance"),

  NO_READ_ACCESS_TO_ARTIFACT("noReadAccessToArtifact"),
  NO_WRITE_ACCESS_TO_ARTIFACT("noWriteAccessToArtifact"),

  NO_WRITE_ACCESS_TO_CATEGORY("noWriteAccessToCategory"),

  FOLDER_NOT_FOUND("folderNotFound"),

  ARTIFACT_NOT_FOUND("artifactNotFound"),

  NODE_NOT_FOUND("nodeNotFound"),

  UNKNOWN_RESOURCE_TYPE("unknownResourceType"),

  MISSING_PARAMETER("missingParameter"),

  INVALID_INPUT("invalidInput"),

  SOURCE_FOLDER_NOT_FOUND("sourceFolderNotFound"),
  SOURCE_RESOURCE_NOT_FOUND("sourceResourceNotFound"),
  TARGET_FOLDER_NOT_FOUND("targetFolderNotFound"),

  NODE_NOT_MOVED("nodeNotMoved"),
  GROUP_NOT_FOUND("groupNotFound"),
  USER_NOT_FOUND("userNotFound"),

  UNIQUE_CONSTRAINT_COLLISION("uniqueConstraintCollision"),
  INVALID_DATA("invalidData"),
  NOT_AUTHORIZED("notAuthorized"),

  PERMISSION_MISSING("permissionMissing"),
  TOKEN_INVALID("tokenInvalid"),
  USER_INFO_LOAD_BY_TOKEN_FAILED("userInfoLoadByTokenFailed"),
  USER_INFO_LOAD_BY_API_KEY_FAILED("userInfoLoadByApiKeyFailed"),
  CEDAR_USER_NOT_FOUND("cedarUserNotFound"),
  AUTHORIZATION_NOT_FOUND("authorizationNotFound"),
  PERMISSION_NOT_OWNED("permissionNotOwned"),
  AUTHORIZATION_TYPE_UNKNOWN("authorizationTypeUnknown"),
  API_KEY_NOT_FOUND("apiKeyNotFound"),
  TOKEN_MISSING("tokenMissing"),
  TOKEN_EXPIRED("tokenExpired"),
  PARENT_FOLDER_NOT_SPECIFIED("parentFolderNotSpecified"),
  PARENT_FOLDER_SPECIFIED_TWICE("parentFolderSpecifiedTwice"),
  PATH_NOT_NORMALIZED("pathNotNormalized"),
  PARENT_FOLDER_NOT_FOUND("parentFolderNotFound"),
  UPDATE_INVALID_FOLDER_NAME("updateInvalidFolderName"),
  CREATE_INVALID_FOLDER_NAME("createInvalidFolderName"),
  NODE_ALREADY_PRESENT("nodeAlreadyPresent"),
  FOLDER_NOT_CREATED("folderNotCreated"),
  MISSING_NAME_AND_DESCRIPTION("missingNameAndDescription"),
  MISSING_DATA("missingData"),
  FOLDER_NOT_DELETED("folderNotDeleted"),
  ARTIFACT_NOT_DELETED("artifactNotDeleted"),
  RESOURCE_NOT_CREATED("resourceNotCreated"),
  INVALID_RESOURCE_TYPE("invalidResourceType"),
  INVALID_ARTIFACT_TYPE("invalidArtifactType"),
  READ_OTHER_PROFILE_FORBIDDEN("readOtherProfileForbidden"),
  UPDATE_OTHER_PROFILE_FORBIDDEN("updateOtherProfileForbidden"),
  FOLDER_CAN_NOT_BE_DELETED("folderCanNotBeDeleted"),
  GROUP_ALREADY_PRESENT("groupAlreadyPresent"),
  GROUP_CAN_BY_MODIFIED_ONLY_BY_GROUP_ADMIN("groupCanBeModifiedOnlyByGroupAdmin"),
  GROUP_CAN_BY_DELETED_ONLY_BY_GROUP_ADMIN("groupCanBeDeletedOnlyByGroupAdmin"),
  SPECIAL_GROUP_CAN_NOT_BE_DELETED("specialGroupCanNotBeDeleted"),
  FOLDER_PERMISSIONS_CAN_NOT_BE_CHANGED("folderPermissionsCanNotBeChanged"),
  UNKNOWN_INSTANCE_OUTPUT_FORMAT("unknownInstanceOutputFormat"),
  FOLDER_COPY_NOT_ALLOWED("folderCopyNotAllowed"),
  METHOD_NOT_IMPLEMENTED("methodNotImplemented"),
  UPSTREAM_SERVER_ERROR("upstreamServerError"),
  NOTHING_TO_DO("nothingToDo"),
  PARENT_CATEGORY_NOT_FOUND("parentCategoryNotFound"),
  CATEGORY_ALREADY_PRESENT("categoryAlreadyPresent"),
  CATEGORY_NOT_FOUND("categoryNotFound"),
  ROOT_CATEGORY_CAN_NOT_BE_DELETED("rootCategoryCanNotBeDeleted"),
  UNABLE_TO_ATTACH_CATEGORY("unableToAttachCategory"),
  UNABLE_TO_DETACH_CATEGORY("unableToDetachCategory"),
  NO_CATEGORIES_WERE_ATTACHED("noCategoriesWereAttached"),

  MALFORMED_JSON_REQUEST_BODY("malformedJsonRequestBody"),
  MALFORMED_SEARCH_SYNTAX("malformedSearchTerm"),

  PUBLISHED_ARTIFACT_CAN_NOT_BE_CHANGED("publishedArtifactCanNotBeChanged"),
  PUBLISHED_ARTIFACT_CAN_NOT_BE_DELETED("publishedArtifactCanNotBeDeleted"),
  VERSIONING_ONLY_ON_LATEST("versioningOnlyOnLatest"),
  VERSIONING_ONLY_BY_OWNER("versioningOnlyByOwner"),
  NON_VERSIONED_ARTIFACT_TYPE("nonVersionedArtifactType"),
  CREATE_DRAFT_ONLY_FROM_PUBLISHED("createDraftOnlyFromPublished"),
  PUBLISH_ONLY_DRAFT("publishOnlyDraft"),

  DRAFT_NOT_CREATED("draftNotCreated"),

  CONTENT_NOT_VALID("contentNotValid"),

  DOI_NOT_SUPPORTED_BY_RESOURCE_TYPE("doiNotSupportedByResourceType"),
  DOI_CAN_NOT_BE_SET_FOR_EMPTY_ATID("doiCanNotBeSetForEmptyAtId"),
  DOI_CAN_NOT_BE_ALTERED("doiCanNotBeAltered"),
  DOI_CAN_NOT_BE_SET("doiCanNotBeSet"),
  DOI_ALREADY_EXISTS("doiAlreadyExists"),
  DATACITE_DOI_DISABLED("dataCiteDOIDisabled"),


  RESOURCE_NOT_FOUND("resourceNotFound"),
  INTERNAL_ERROR("internalError");

  private final String value;

  CedarErrorKey(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

}
