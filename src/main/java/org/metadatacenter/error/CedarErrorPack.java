package org.metadatacenter.error;

import org.elasticsearch.ElasticsearchException;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.operation.CedarOperationDescriptor;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

public class CedarErrorPack {

  private Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
  private CedarErrorType errorType = CedarErrorType.NONE;
  private CedarErrorKey errorKey = CedarErrorKey.NONE;
  private CedarErrorReasonKey errorReasonKey = CedarErrorReasonKey.NONE;
  private String message;
  private final Map<String, Object> parameters;
  private final Map<String, Object> objects;
  private final Map<String, Object> entities;
  private CedarSuggestedAction suggestedAction = CedarSuggestedAction.NONE;
  private Exception originalException;
  private CedarErrorPackException sourceException;
  private CedarOperationDescriptor operation;

  public CedarErrorPack() {
    parameters = new HashMap<>();
    entities = new HashMap<>();
    objects = new HashMap<>();
  }

  public CedarErrorPack(CedarErrorPack other) {
    this();
    if (other != null) {
      status = other.getStatus();
      errorType = other.getErrorType();
      errorKey = other.getErrorKey();
      errorReasonKey = other.getErrorReasonKey();
      message = other.getMessage();
      parameters.putAll(other.getParameters());
      objects.putAll(other.getObjects());
      entities.putAll(other.getEntities());
      suggestedAction = other.getSuggestedAction();
      originalException = normalizeException(other.getOriginalException());
      sourceException = other.getSourceException();
      operation = other.getOperation();
    }
  }

  public void merge(CedarErrorPack other) {
    if (other.status != null) {
      status = other.getStatus();
    }
    if (other.errorType != null) {
      errorType = other.getErrorType();
    }
    if (other.errorKey != null) {
      errorKey = other.getErrorKey();
    }
    if (other.errorReasonKey != null) {
      errorReasonKey = other.getErrorReasonKey();
    }
    if (other.message != null) {
      message = other.getMessage();
    }
    if (!other.parameters.isEmpty()) {
      parameters.clear();
      parameters.putAll(other.getParameters());
    }
    if (!other.objects.isEmpty()) {
      objects.clear();
      objects.putAll(other.getObjects());
    }
    if (!other.entities.isEmpty()) {
      entities.clear();
      entities.putAll(other.getEntities());
    }
    if (other.suggestedAction != null) {
      suggestedAction = other.getSuggestedAction();
    }
    if (other.originalException != null) {
      originalException = normalizeException(other.getOriginalException());
    }
    if (other.sourceException != null) {
      sourceException = other.getSourceException();
    }
    if (other.operation != null) {
      operation = other.getOperation();
    }
  }

  public Response.Status getStatus() {
    return status;
  }

  public CedarErrorPack status(Response.Status status) {
    this.status = status;
    return this;
  }

  public CedarErrorType getErrorType() {
    return errorType;
  }

  public CedarErrorPack errorType(CedarErrorType errorType) {
    this.errorType = errorType;
    return this;
  }

  public CedarErrorKey getErrorKey() {
    return errorKey;
  }

  public CedarErrorPack errorKey(CedarErrorKey errorKey) {
    this.errorKey = errorKey;
    return this;
  }

  public CedarErrorReasonKey getErrorReasonKey() {
    return errorReasonKey;
  }

  public CedarErrorPack errorReasonKey(CedarErrorReasonKey errorReasonKey) {
    this.errorReasonKey = errorReasonKey;
    return this;
  }

  public String getMessage() {
    return message;
  }

  public CedarErrorPack message(String message) {
    this.message = message;
    return this;
  }

  public Map<String, Object> getParameters() {
    return parameters;
  }

  public Map<String, Object> getObjects() {
    return objects;
  }

  public Map<String, Object> getEntities() {
    return entities;
  }

  public CedarErrorPack parameter(String name, Object value) {
    this.parameters.put(name, value);
    return this;
  }

  public CedarErrorPack object(String name, Object value) {
    this.objects.put(name, value);
    return this;
  }

  public CedarErrorPack entity(String name, Object value) {
    this.entities.put(name, value);
    return this;
  }

  public CedarSuggestedAction getSuggestedAction() {
    return suggestedAction;
  }

  public CedarErrorPack suggestedAction(CedarSuggestedAction suggestedAction) {
    this.suggestedAction = suggestedAction;
    return this;
  }

  public CedarErrorPackException getSourceException() {
    return sourceException;
  }

  public CedarErrorPack sourceException(Exception sourceException) {
    this.originalException = normalizeException(sourceException);
    this.sourceException = new CedarErrorPackException(normalizeException(sourceException));
    return this;
  }

  public Exception getOriginalException() {
    return originalException;
  }

  public void sourceException(CedarErrorPackException sourceException) {
    this.sourceException = sourceException;
  }

  public CedarOperationDescriptor getOperation() {
    return operation;
  }

  public CedarErrorPack operation(CedarOperationDescriptor operation) {
    this.operation = operation;
    return this;
  }

  public void resetSourceException() {
    this.originalException = null;
    this.sourceException = null;
  }

  public static Exception normalizeException(Exception otherException) {
    if (otherException == null) {
      return null;
    }
    if (otherException instanceof ElasticsearchException) {
      if (otherException == ((ElasticsearchException) otherException).getRootCause()) {
        return new CedarFixedRecursiveException(otherException);
      }
    }
    //TODO: Possibly we need to do this unwrapping for all the CedarProcessingException exceptions
    if (otherException instanceof CedarProcessingException) {
      Throwable cause = otherException.getCause();
      if (cause instanceof ElasticsearchException) {
        return new CedarProcessingException(otherException.getMessage(), new CedarFixedRecursiveException(cause));
      }
    }
    return otherException;
  }

}
