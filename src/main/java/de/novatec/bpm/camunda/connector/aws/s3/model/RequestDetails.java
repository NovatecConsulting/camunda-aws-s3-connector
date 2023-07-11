package de.novatec.bpm.camunda.connector.aws.s3.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
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
}
