package de.novatec.bpm.camunda.connector.aws.s3.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileContent {

    private byte[] content;
    private String contentType;
    private Long contentLength;

}
