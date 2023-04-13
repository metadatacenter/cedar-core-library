package org.metadatacenter.server.security.model;

import org.metadatacenter.model.BiboStatus;

public interface NodeWithPublicationStatus {

  BiboStatus getPublicationStatus();
}
