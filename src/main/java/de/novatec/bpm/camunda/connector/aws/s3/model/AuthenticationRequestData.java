package de.novatec.bpm.camunda.connector.aws.s3.model;

import io.camunda.connector.api.annotation.Secret;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AuthenticationRequestData {
  @NotEmpty
  @Secret
  private String accessKey;
  @NotEmpty
  @Secret
  private String secretKey;


}
