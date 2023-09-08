package de.novatec.bpm.camunda.connector.aws.s3.adapter.in.model;

import de.novatec.bpm.camunda.connector.aws.s3.domain.model.RequestData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConnectorResponse {

    private String bucketName;
    private String objectKey;
    private String filePath;

    public ConnectorResponse(RequestData request) {
        this.bucketName = request.getBucket();
        this.objectKey = request.getKey();
        this.filePath = request.getFilePath();
    }

}
