package de.novatec.bpm.camunda.connector.aws.s3.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestData {

    private String accessKey;
    private String secretKey;
    private String bucket;
    private String key;
    private String region;
    private String filePath;
    private String contentType;

}
