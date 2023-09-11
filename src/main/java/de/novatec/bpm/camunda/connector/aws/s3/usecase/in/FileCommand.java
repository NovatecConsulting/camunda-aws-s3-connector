package de.novatec.bpm.camunda.connector.aws.s3.usecase.in;

import de.novatec.bpm.camunda.connector.aws.s3.domain.model.RequestData;

import java.io.IOException;

public interface FileCommand {

    RequestData uploadFile(RequestData request) throws IOException;

    RequestData deleteFile(RequestData request) throws IOException;

    RequestData downloadFile(RequestData request) throws IOException;

}
