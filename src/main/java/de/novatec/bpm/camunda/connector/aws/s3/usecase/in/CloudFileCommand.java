package de.novatec.bpm.camunda.connector.aws.s3.usecase.in;

import de.novatec.bpm.camunda.connector.aws.s3.domain.model.S3RequestData;

import java.io.IOException;

public interface CloudFileCommand {

    S3RequestData uploadFile(S3RequestData request) throws IOException;

    S3RequestData deleteFile(S3RequestData request) throws IOException;

    S3RequestData downloadFile(S3RequestData request) throws IOException;

}
