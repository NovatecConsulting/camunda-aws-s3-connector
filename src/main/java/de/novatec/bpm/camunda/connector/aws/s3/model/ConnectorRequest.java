package de.novatec.bpm.camunda.connector.aws.s3.model;

import io.camunda.connector.api.annotation.Secret;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ConnectorRequest {

  @Valid
  @NotNull
  @Secret
  private AuthenticationRequestData authentication;
  @Valid
  @NotNull
  private RequestDetails requestDetails;

  public AuthenticationRequestData getAuthentication() {
    return authentication;
  }

  public void setAuthentication(AuthenticationRequestData authentication) {
    this.authentication = authentication;
  }

  public RequestDetails getRequestDetails() {
    return requestDetails;
  }

  public void setRequestDetails(RequestDetails requestDetails) {
    this.requestDetails = requestDetails;
  }

  @Override
  public String toString() {
    return "ConnectorRequest{" +
            "authentication=" + authentication +
            ", requestDetails=" + requestDetails +
            '}';
  }
}
