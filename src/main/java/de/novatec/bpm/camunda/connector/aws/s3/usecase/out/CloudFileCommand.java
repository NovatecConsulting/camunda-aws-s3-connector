package de.novatec.bpm.camunda.connector.aws.s3.usecase.out;

import de.novatec.bpm.camunda.connector.aws.s3.domain.model.FileContent;
import de.novatec.bpm.camunda.connector.aws.s3.domain.model.RequestData;

import java.io.IOException;

public interface CloudFileCommand {
    void deleteObject(RequestData requestData);
    void putObject(RequestData requestData, FileContent fileContent) throws IOException;
    FileContent getObject(RequestData requestData) throws IOException;
}
