package de.novatec.bpm.camunda.connector.aws.s3.adapter.in.process;

import de.novatec.bpm.camunda.connector.aws.s3.adapter.in.process.model.ConnectorRequest;
import de.novatec.bpm.camunda.connector.file.api.impl.model.RequestData;

import java.util.Objects;

public class RequestMapper {

    public static RequestData mapRequest(ConnectorRequest request) {
        return RequestData.builder()
                .authenticationKey(request.getAuthentication().getAccessKey())
                .authenticationSecret(request.getAuthentication().getSecretKey())
                .region(request.getRequestDetails().getRegion())
                .bucket(request.getRequestDetails().getBucketName())
                .key(request.getRequestDetails().getObjectKey())
                .filePath(
                        // fallback to objectKey
                        Objects.requireNonNullElse(
                                request.getRequestDetails().getFilePath(),
                                request.getRequestDetails().getObjectKey()
                        )
                )
                .contentType(request.getRequestDetails().getContentType())
                .build();
    }

}
