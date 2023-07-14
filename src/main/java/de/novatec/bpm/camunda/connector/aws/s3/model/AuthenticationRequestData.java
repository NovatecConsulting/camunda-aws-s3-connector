package de.novatec.bpm.camunda.connector.aws.s3.model;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Data
public class AuthenticationRequestData {

  @NotEmpty
  @ToString.Exclude
  private String accessKey;

  @NotEmpty
  @ToString.Exclude
  private String secretKey;

}
