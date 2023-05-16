package de.novatec.bpm.camunda.connector.aws.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import de.novatec.bpm.camunda.connector.aws.s3.model.AuthenticationRequestData;

public class S3ServiceFactory {

    public static S3Service getService(AuthenticationRequestData auth, String region) {
        return new S3ServiceImpl(auth, region);
    }
    public static S3Service getService(AmazonS3 client) {
        return new S3ServiceImpl(client);
    }

}
