package de.novatec.bpm.camunda.connector.aws.s3.service;

import de.novatec.bpm.camunda.connector.aws.s3.model.AuthenticationRequestData;
import de.novatec.bpm.camunda.connector.aws.s3.model.ConnectorResponse;
import de.novatec.bpm.camunda.connector.aws.s3.model.RequestDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.*;
import java.util.Objects;

public class S3ServiceImpl implements S3Service {

    private static final Logger logger = LoggerFactory.getLogger(S3ServiceImpl.class);
    private final S3Client s3Client;

    protected S3ServiceImpl(AuthenticationRequestData authentication, String region) {
        s3Client = getClient(authentication, region);
    }

    protected S3ServiceImpl(S3Client client) {
        s3Client = client;
    }

    private S3Client getClient(AuthenticationRequestData authentication, String region) {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(authentication.getAccessKey(), authentication.getSecretKey());
        return S3Client.builder()
                .credentialsProvider(() -> StaticCredentialsProvider.create(credentials).resolveCredentials())
                .region(Region.of(region))
                .build();
    }

    public ConnectorResponse deleteObject(RequestDetails details) {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(details.getBucketName())
                .key(details.getObjectKey())
                .build();
        logger.info("Delete object: {}", details.getBucketName()+"/"+details.getObjectKey());
        logger.debug("request {}", deleteRequest);
        s3Client.deleteObject(deleteRequest);
        logger.info("Object deleted: {}", details.getBucketName()+"/"+details.getObjectKey());

        return new ConnectorResponse();
    }

    public ConnectorResponse putObject(RequestDetails details) throws IOException {
        String filePath = Objects.requireNonNull(details.getFilePath(), "File path variable is required for operation PUT");
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] objectBytes = fis.readAllBytes();
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(details.getBucketName())
                    .key(details.getObjectKey())
                    .contentType(Objects.requireNonNull(details.getContentType(), "Content type variable is required for operation PUT"))
                    .contentLength((long) objectBytes.length)
                    .build();
            logger.info("Put object: {}", details.getBucketName()+"/"+details.getObjectKey());
            logger.debug("request {}", request);
            PutObjectResponse response = s3Client.putObject(request, RequestBody.fromBytes(objectBytes));
            logger.info("Object put: {}", details.getBucketName()+"/"+details.getObjectKey());
            logger.debug("response {}", response);

            return new ConnectorResponse(response);
        }
    }

}
