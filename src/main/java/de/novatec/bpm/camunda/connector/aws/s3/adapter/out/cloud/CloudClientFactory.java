package de.novatec.bpm.camunda.connector.aws.s3.adapter.out.cloud;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

public class CloudClientFactory {

    public S3Client createClient(String accessKey, String secretKey, String region) {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        return S3Client.builder()
                .credentialsProvider(() -> StaticCredentialsProvider.create(credentials).resolveCredentials())
                .region(Region.of(region))
                .build();
    }

}
