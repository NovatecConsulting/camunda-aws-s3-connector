package de.novatec.bpm.camunda.connector.aws.s3.model;

import javax.validation.constraints.NotEmpty;

public class RequestDetails {

  @NotEmpty
  private String bucketName;
  @NotEmpty
  private String objectKey;
  private String content;
  @NotEmpty
  private String region;
  private OperationType operationType;
  private boolean encryption;

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

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
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

}
