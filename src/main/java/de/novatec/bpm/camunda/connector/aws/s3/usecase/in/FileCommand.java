package de.novatec.bpm.camunda.connector.aws.s3.usecase.in;

import de.novatec.bpm.camunda.connector.aws.s3.domain.model.RequestData;

import java.io.IOException;

public interface FileCommand {

    RequestData writeFile(RequestData request) throws IOException;

    RequestData deleteFile(RequestData request);

    RequestData loadFile(RequestData request) throws IOException;

}
