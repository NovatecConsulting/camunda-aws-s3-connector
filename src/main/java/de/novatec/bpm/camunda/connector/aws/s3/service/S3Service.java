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

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.Objects;

public class S3Service {

    private final AmazonS3 s3Client;

    public S3Service(AuthenticationRequestData authentication, String region) {
        s3Client = getClient(authentication, region);
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

    public ConnectorResponse putObject(RequestDetails details) {
        String content = Objects.requireNonNull(details.getContent(), "Content variable is required for operation PUT");
        byte[] objectBytes = Base64.getDecoder().decode(content.getBytes());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(objectBytes.length);
        objectMetadata.setContentType(Objects.requireNonNull(details.getContentType(), "Content type variable is required for operation PUT"));
        objectMetadata.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);
        PutObjectRequest putRequest = new PutObjectRequest(
                details.getBucketName(),
                details.getObjectKey(),
                new ByteArrayInputStream(objectBytes),
                objectMetadata
        );
        PutObjectResult putObjectResult = s3Client.putObject(putRequest);
        return new ConnectorResponse(putObjectResult);
    }

}
