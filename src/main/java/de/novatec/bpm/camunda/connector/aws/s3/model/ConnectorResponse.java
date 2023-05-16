package de.novatec.bpm.camunda.connector.aws.s3.model;

import com.amazonaws.services.s3.model.PutObjectResult;

import java.util.Objects;

public class ConnectorResponse {
  private String encryptionStatus;
  private String md5;
  private String version;

  public ConnectorResponse(final PutObjectResult putObjectResult) {
    this.encryptionStatus = putObjectResult.getSSEAlgorithm();
    this.md5 = putObjectResult.getContentMd5();
    this.version = putObjectResult.getVersionId();
  }

  public ConnectorResponse() {
  }

  public String getEncryptionStatus() {
    return encryptionStatus;
  }

  public void setEncryptionStatus(String encryptionStatus) {
    this.encryptionStatus = encryptionStatus;
  }

  public String getMd5() {
    return md5;
  }

  public void setMd5(String md5) {
    this.md5 = md5;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ConnectorResponse that = (ConnectorResponse) o;
    return Objects.equals(encryptionStatus, that.encryptionStatus) && Objects.equals(md5, that.md5) && Objects.equals(version, that.version);
  }

  @Override
  public int hashCode() {
    return Objects.hash(encryptionStatus, md5, version);
  }

  @Override
  public String toString() {
    return "ConnectorResponse{" +
            "encryptionStatus='" + encryptionStatus + '\'' +
            ", md5='" + md5 + '\'' +
            ", version='" + version + '\'' +
            '}';
  }
}
