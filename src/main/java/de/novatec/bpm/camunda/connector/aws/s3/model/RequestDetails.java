package de.novatec.bpm.camunda.connector.aws.s3.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class RequestDetails {

  @NotEmpty
  private String bucketName;
  @NotEmpty
  private String objectKey;
  private String filePath;
  @NotEmpty
  private String region;
  @NotNull
  private OperationType operationType;
  private String contentType;

  public String getBucketName() {
    return bucketName;
  }

  public void setBucketName(String bucketName) {
    this.bucketName = bucketName;
  }

  public String getObjectKey() {
    return objectKey;
  }

  public void setObjectKey(String objectKey) {
    this.objectKey = objectKey;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public OperationType getOperationType() {
    return operationType;
  }

  public void setOperationType(OperationType operationType) {
    this.operationType = operationType;
  }

  @Override
  public String toString() {
    return "RequestDetails{" +
            "bucketName='" + bucketName + '\'' +
            ", objectKey='" + objectKey + '\'' +
            ", filePath='" + filePath + '\'' +
            ", region='" + region + '\'' +
            ", operationType=" + operationType +
            ", contentType='" + contentType + '\'' +
            '}';
  }
}
