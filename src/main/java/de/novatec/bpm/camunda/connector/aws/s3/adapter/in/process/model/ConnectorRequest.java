package de.novatec.bpm.camunda.connector.aws.s3.adapter.in.process.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConnectorRequest {

  @Valid
  @NotNull
  private AuthenticationRequestData authentication;

  @Valid
  @NotNull
  private RequestDetails requestDetails;
}
