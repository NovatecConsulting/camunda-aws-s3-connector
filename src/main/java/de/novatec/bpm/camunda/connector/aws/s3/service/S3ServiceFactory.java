package de.novatec.bpm.camunda.connector.aws.s3.service;

import de.novatec.bpm.camunda.connector.aws.s3.model.AuthenticationRequestData;
import software.amazon.awssdk.services.s3.S3Client;

public class S3ServiceFactory {

    public static S3Service getService(AuthenticationRequestData auth, String region) {
        return new S3ServiceImpl(auth, region);
    }
    public static S3Service getService(S3Client client) {
        return new S3ServiceImpl(client);
    }

}
