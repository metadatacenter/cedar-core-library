package org.metadatacenter.exception;

import org.metadatacenter.http.CedarResponseStatus;

/**
 * A request could not be completed because a downstream service was unavailable.
 *
 * <p>This is deliberately distinct from {@link CedarProcessingException}. A connection refusal,
 * timeout, or other transport failure is a temporary dependency outage, not an unexplained defect
 * in the service handling the request. The ordinary CEDAR exception mapper therefore returns 503
 * from the status carried by this exception instead of turning the outage into a generic 500.</p>
 */
public class CedarDependencyUnavailableException extends CedarProcessingException {

  public CedarDependencyUnavailableException(String message, Exception sourceException) {
    super(message, sourceException);
    getErrorPack().status(CedarResponseStatus.SERVICE_UNAVAILABLE);
  }

  public CedarDependencyUnavailableException(Exception sourceException) {
    this(sourceException.getMessage(), sourceException);
  }
}
