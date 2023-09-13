package de.novatec.bpm.camunda.connector.aws.s3.adapter.out.cloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

public class CloudClientFactory {

    private static final Logger logger = LoggerFactory.getLogger(CloudClientFactory.class);

    public S3Client createClient(String accessKey, String secretKey, String region) {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        logger.info("Initialized AWS client for region {}", region);
        return S3Client.builder()
                .credentialsProvider(() -> StaticCredentialsProvider.create(credentials).resolveCredentials())
                .region(Region.of(region))
                .build();
    }

}
