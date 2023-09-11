package de.novatec.bpm.camunda.connector.aws.s3.adapter.out.cloud;

import de.novatec.bpm.camunda.connector.aws.s3.domain.model.FileContent;
import de.novatec.bpm.camunda.connector.aws.s3.domain.model.RequestData;
import de.novatec.bpm.camunda.connector.aws.s3.usecase.out.CloudFileCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.*;

public class CloudFileAdapter implements CloudFileCommand {

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
            s3Client.deleteObject(awsRequest);
        }
        logger.info("Object deleted: {}", requestData.getBucket() + "/" + requestData.getKey());
    }

    public void putObject(RequestData putRequest, FileContent fileContent) {
        try (S3Client s3Client = clientFactory.createClient(putRequest.getAccessKey(), putRequest.getSecretKey(), putRequest.getRegion())) {
            PutObjectRequest awsRequest = PutObjectRequest.builder()
                    .bucket(putRequest.getBucket())
                    .key(putRequest.getKey())
                    .contentType(fileContent.getContentType())
                    .contentLength(fileContent.getContentLength())
                    .build();
            logger.info("Put object: {}", putRequest.getBucket() + "/" + putRequest.getKey());
            logger.debug("request {}", awsRequest);
            PutObjectResponse awsResponse = s3Client.putObject(awsRequest, RequestBody.fromBytes(fileContent.getContent()));
            logger.info("Object put: {}", putRequest.getBucket() + "/" + putRequest.getKey());
            logger.debug("response {}", awsResponse);
        }
    }

    @Override
    public FileContent getObject(RequestData getRequest) throws IOException {
        try (S3Client s3Client = clientFactory.createClient(getRequest.getAccessKey(), getRequest.getSecretKey(), getRequest.getRegion())) {
            GetObjectRequest awsRequest = GetObjectRequest.builder()
                    .bucket(getRequest.getBucket())
                    .key(getRequest.getKey())
                    .build();
            ResponseInputStream<GetObjectResponse> object = s3Client.getObject(awsRequest);
            return FileContent.builder()
                    .content(object.readAllBytes())
                    .contentType(object.response().contentType())
                    .contentLength(object.response().contentLength())
                    .build();
        }
    }

}
