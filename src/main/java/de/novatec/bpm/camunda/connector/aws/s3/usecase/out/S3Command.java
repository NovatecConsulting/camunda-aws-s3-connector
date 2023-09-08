package de.novatec.bpm.camunda.connector.aws.s3.usecase.out;

import de.novatec.bpm.camunda.connector.aws.s3.domain.model.RequestData;

import java.io.IOException;

public interface S3Command {
    void deleteObject(RequestData requestData);
    void putObject(RequestData requestData) throws IOException;
    byte[] getObject(RequestData requestData) throws IOException;
}
