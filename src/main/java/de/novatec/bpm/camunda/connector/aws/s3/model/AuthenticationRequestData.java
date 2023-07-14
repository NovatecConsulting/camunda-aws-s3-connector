package de.novatec.bpm.camunda.connector.aws.s3.model;

import io.camunda.connector.api.annotation.Secret;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

@Data
public class AuthenticationRequestData {
  @NotEmpty
  @ToString.Exclude
  private String accessKey;

  @NotEmpty
  @ToString.Exclude
  private String secretKey;

}
