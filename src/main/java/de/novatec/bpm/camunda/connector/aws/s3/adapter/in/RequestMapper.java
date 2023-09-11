package de.novatec.bpm.camunda.connector.aws.s3.adapter.in;

import de.novatec.bpm.camunda.connector.aws.s3.adapter.in.model.ConnectorRequest;
import de.novatec.bpm.camunda.connector.aws.s3.domain.model.S3RequestData;

public class RequestMapper {

    public static S3RequestData mapRequest(ConnectorRequest request) {
        return S3RequestData.builder()
                .accessKey(request.getAuthentication().getAccessKey())
                .secretKey(request.getAuthentication().getSecretKey())
                .region(request.getRequestDetails().getRegion())
                .bucket(request.getRequestDetails().getBucketName())
                .key(request.getRequestDetails().getObjectKey())
                .filePath(request.getRequestDetails().getFilePath())
                .contentType(request.getRequestDetails().getContentType())
                .build();
    }

}
