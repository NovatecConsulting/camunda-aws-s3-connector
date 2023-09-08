package de.novatec.bpm.camunda.connector.aws.s3.adapter.out;

import de.novatec.bpm.camunda.connector.aws.s3.domain.model.RequestData;
import de.novatec.bpm.camunda.connector.aws.s3.usecase.out.S3Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.*;
import java.util.Objects;

public class S3Adapter implements S3Command {

    private static final Logger logger = LoggerFactory.getLogger(S3Adapter.class);

    private final S3ClientFactory clientFactory;

    public S3Adapter(S3ClientFactory clientFactory) {
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

    public void putObject(RequestData putRequest) throws IOException {
        try (S3Client s3Client = clientFactory.createClient(putRequest.getAccessKey(), putRequest.getSecretKey(), putRequest.getRegion())) {
            String filePath = Objects.requireNonNull(putRequest.getFilePath(), "File path variable is required for operation PUT");
            try (FileInputStream fis = new FileInputStream(filePath)) {
                byte[] objectBytes = fis.readAllBytes();
                PutObjectRequest awsRequest = PutObjectRequest.builder()
                        .bucket(putRequest.getBucket())
                        .key(putRequest.getKey())
                        .contentType(Objects.requireNonNull(putRequest.getContentType(), "Content type variable is required for operation PUT"))
                        .contentLength((long) objectBytes.length)
                        .build();
                logger.info("Put object: {}", putRequest.getBucket() + "/" + putRequest.getKey());
                logger.debug("request {}", awsRequest);
                PutObjectResponse awsResponse = s3Client.putObject(awsRequest, RequestBody.fromBytes(objectBytes));
                logger.info("Object put: {}", putRequest.getBucket() + "/" + putRequest.getKey());
                logger.debug("response {}", awsResponse);
            }
        }
    }

    @Override
    public byte[] getObject(RequestData getRequest) throws IOException {
        try (S3Client s3Client = clientFactory.createClient(getRequest.getAccessKey(), getRequest.getSecretKey(), getRequest.getRegion())) {
            GetObjectRequest awsRequest = GetObjectRequest.builder()
                    .bucket(getRequest.getBucket())
                    .key(getRequest.getKey())
                    .checksumMode(ChecksumMode.ENABLED)
                    .build();
            return s3Client.getObject(awsRequest).readAllBytes();
        }
    }

}
