package info.novatec.bpm.camunda.connector.aws.s3.adapter.in.process.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.ToString;

@Data
public class AuthenticationRequestData {

  @NotEmpty
  @ToString.Exclude
  private String accessKey;

  @NotEmpty
  @ToString.Exclude
  private String secretKey;

}
