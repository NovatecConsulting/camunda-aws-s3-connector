package de.novatec.bpm.camunda.connector.aws.s3.model;

import io.camunda.connector.api.annotation.Secret;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class ConnectorRequest {

  @Valid
  @NotNull
  @Secret
  private AuthenticationRequestData authentication;
  @Valid
  @NotNull
  private RequestDetails requestDetails;
}
