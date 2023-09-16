package de.novatec.bpm.camunda.connector.aws.s3.adapter.out.cloud;

import de.novatec.bpm.camunda.connector.file.api.RemoteFileCommand;
import de.novatec.bpm.camunda.connector.file.api.impl.model.FileContent;
import de.novatec.bpm.camunda.connector.file.api.impl.model.RequestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;

public class CloudFileAdapter implements RemoteFileCommand {

    private static final Logger logger = LoggerFactory.getLogger(CloudFileAdapter.class);

    private final CloudClientFactory clientFactory;

    public CloudFileAdapter(CloudClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    public void deleteObject(RequestData requestData) {
        try (S3Client s3Client = clientFactory.createClient(requestData.getAccessKey(), requestData.getSecretKey(), requestData.getRegion())) {
            DeleteObjectRequest awsRequest = DeleteObjectRequest.builder()
                    .bucket(requestData.getBucket())
                    .key(requestData.getKey())
                    .build();
            logger.info("Delete object: {}", requestData.getBucket() + "/" + requestData.getKey());
            logger.debug("request {}", awsRequest);
            DeleteObjectResponse response = s3Client.deleteObject(awsRequest);
            logger.info("Object deleted: {}", requestData.getBucket() + "/" + requestData.getKey());
            logger.debug("response {}", response);
        }
    }

    public void putObject(RequestData requestData, FileContent fileContent) {
        try (S3Client s3Client = clientFactory.createClient(requestData.getAccessKey(), requestData.getSecretKey(), requestData.getRegion())) {
            PutObjectRequest awsRequest = PutObjectRequest.builder()
                    .bucket(requestData.getBucket())
                    .key(requestData.getKey())
                    .contentType(fileContent.getContentType())
                    .contentLength(fileContent.getContentLength())
                    .build();
            logger.info("Put object: {}", requestData.getBucket() + "/" + requestData.getKey());
            logger.debug("request {}", awsRequest);
            PutObjectResponse awsResponse = s3Client.putObject(awsRequest, RequestBody.fromBytes(fileContent.getContent()));
            logger.info("Object put: {}", requestData.getBucket() + "/" + requestData.getKey());
            logger.debug("response {}", awsResponse);
        }
    }

    @Override
    public FileContent getObject(RequestData requestData) throws IOException {
        try (S3Client s3Client = clientFactory.createClient(requestData.getAccessKey(), requestData.getSecretKey(), requestData.getRegion())) {
            GetObjectRequest awsRequest = GetObjectRequest.builder()
                    .bucket(requestData.getBucket())
                    .key(requestData.getKey())
                    .build();
            logger.info("Get object: {}", requestData.getBucket() + "/" + requestData.getKey());
            logger.debug("request {}", awsRequest);
            try (ResponseInputStream<GetObjectResponse> object = s3Client.getObject(awsRequest)) {
                FileContent result = FileContent.builder()
                        .content(object.readAllBytes())
                        .contentType(object.response().contentType())
                        .contentLength(object.response().contentLength())
                        .build();
                logger.info("Object received: {}", requestData.getBucket() + "/" + requestData.getKey());
                logger.debug("response {}", object.response());
                return result;
            }
        }
    }

}
