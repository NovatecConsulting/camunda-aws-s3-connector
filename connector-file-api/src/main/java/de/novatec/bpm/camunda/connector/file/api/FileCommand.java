package de.novatec.bpm.camunda.connector.file.api;

import de.novatec.bpm.camunda.connector.file.api.impl.model.RequestData;

import java.io.IOException;

public interface FileCommand {

    RequestData uploadFile(RequestData request) throws IOException;

    RequestData deleteFile(RequestData request) throws IOException;

    RequestData downloadFile(RequestData request) throws IOException;

}
