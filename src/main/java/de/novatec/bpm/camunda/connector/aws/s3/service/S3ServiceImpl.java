package de.novatec.bpm.camunda.connector.aws.s3.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import de.novatec.bpm.camunda.connector.aws.s3.model.AuthenticationRequestData;
import de.novatec.bpm.camunda.connector.aws.s3.model.ConnectorResponse;
import de.novatec.bpm.camunda.connector.aws.s3.model.RequestDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Objects;

public class S3ServiceImpl implements S3Service {

    private static final Logger logger = LoggerFactory.getLogger(S3ServiceImpl.class);
    private final AmazonS3 s3Client;

    protected S3ServiceImpl(AuthenticationRequestData authentication, String region) {
        s3Client = getClient(authentication, region);
    }

    protected S3ServiceImpl(AmazonS3 client) {
        s3Client = client;
    }

    private AmazonS3 getClient(AuthenticationRequestData authentication, String region) {
        return AmazonS3ClientBuilder.standard()
                .withCredentials(
                        new AWSStaticCredentialsProvider(
                                new BasicAWSCredentials(authentication.getAccessKey(), authentication.getSecretKey())
                        )
                )
                .withRegion(region)
                .build();
    }

    public ConnectorResponse deleteObject(RequestDetails details) {
        s3Client.deleteObject(details.getBucketName(), details.getObjectKey());
        return new ConnectorResponse();
    }

    public ConnectorResponse putObject(RequestDetails details) throws IOException {
        String filePath = Objects.requireNonNull(details.getFilePath(), "File path variable is required for operation PUT");
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] objectBytes = fis.readAllBytes();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(objectBytes.length);
            objectMetadata.setContentType(Objects.requireNonNull(details.getContentType(), "Content type variable is required for operation PUT"));
            objectMetadata.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);
            logger.debug("object metadata {}", objectMetadata);
            PutObjectRequest putRequest = new PutObjectRequest(
                    details.getBucketName(),
                    details.getObjectKey(),
                    new ByteArrayInputStream(objectBytes),
                    objectMetadata
            );
            logger.debug("request {}", putRequest);
            PutObjectResult putObjectResult = s3Client.putObject(putRequest);
            logger.debug("response {}", putObjectResult);
            return new ConnectorResponse(putObjectResult);
        }
    }

}
