package de.novatec.bpm.camunda.connector.aws.s3.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.util.Objects;

@Data
@NoArgsConstructor
public class ConnectorResponse {
  private String serverSideEncryption;
  private String versionId;

  public ConnectorResponse(final PutObjectResponse response) {
    this.serverSideEncryption = response.serverSideEncryptionAsString();
    this.versionId = response.versionId();
  }

}
