package de.novatec.bpm.camunda.connector.aws.s3.adapter.out;

import de.novatec.bpm.camunda.connector.aws.s3.domain.model.FileContent;
import de.novatec.bpm.camunda.connector.aws.s3.domain.model.S3RequestData;
import de.novatec.bpm.camunda.connector.aws.s3.usecase.out.S3Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.*;

public class S3Adapter implements S3Command {

    private static final Logger logger = LoggerFactory.getLogger(S3Adapter.class);

    private final S3ClientFactory clientFactory;

    public S3Adapter(S3ClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    public void deleteObject(S3RequestData s3RequestData) {
        try (S3Client s3Client = clientFactory.createClient(s3RequestData.getAccessKey(), s3RequestData.getSecretKey(), s3RequestData.getRegion())) {
            DeleteObjectRequest awsRequest = DeleteObjectRequest.builder()
                    .bucket(s3RequestData.getBucket())
                    .key(s3RequestData.getKey())
                    .build();
            logger.info("Delete object: {}", s3RequestData.getBucket() + "/" + s3RequestData.getKey());
            logger.debug("request {}", awsRequest);
            s3Client.deleteObject(awsRequest);
        }
        logger.info("Object deleted: {}", s3RequestData.getBucket() + "/" + s3RequestData.getKey());
    }

    public void putObject(S3RequestData putRequest, FileContent fileContent) {
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
    public FileContent getObject(S3RequestData getRequest) throws IOException {
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
