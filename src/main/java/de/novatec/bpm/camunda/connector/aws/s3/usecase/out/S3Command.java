package de.novatec.bpm.camunda.connector.aws.s3.usecase.out;

import de.novatec.bpm.camunda.connector.aws.s3.domain.model.FileContent;
import de.novatec.bpm.camunda.connector.aws.s3.domain.model.S3RequestData;

import java.io.IOException;

public interface S3Command {
    void deleteObject(S3RequestData s3RequestData);
    void putObject(S3RequestData s3RequestData, FileContent fileContent) throws IOException;
    FileContent getObject(S3RequestData s3RequestData) throws IOException;
}
