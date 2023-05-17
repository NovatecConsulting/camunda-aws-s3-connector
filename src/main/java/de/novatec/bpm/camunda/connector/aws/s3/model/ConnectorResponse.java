package de.novatec.bpm.camunda.connector.aws.s3.model;

import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.util.Objects;

public class ConnectorResponse {
  private String serverSideEncryption;
  private String versionId;

  public ConnectorResponse(final PutObjectResponse response) {
    this.serverSideEncryption = response.serverSideEncryptionAsString();
    this.versionId = response.versionId();
  }

  public ConnectorResponse() {
  }

  public String getServerSideEncryption() {
    return serverSideEncryption;
  }

  public void setServerSideEncryption(String serverSideEncryption) {
    this.serverSideEncryption = serverSideEncryption;
  }

  public String getVersionId() {
    return versionId;
  }

  public void setVersionId(String versionId) {
    this.versionId = versionId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ConnectorResponse that = (ConnectorResponse) o;
    return Objects.equals(serverSideEncryption, that.serverSideEncryption) && Objects.equals(versionId, that.versionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(serverSideEncryption, versionId);
  }

  @Override
  public String toString() {
    return "ConnectorResponse{" +
            "serverSideEncryption='" + serverSideEncryption + '\'' +
            ", versionId='" + versionId + '\'' +
            '}';
  }
}
