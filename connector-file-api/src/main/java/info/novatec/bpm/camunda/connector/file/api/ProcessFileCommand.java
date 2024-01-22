package info.novatec.bpm.camunda.connector.file.api;

import info.novatec.bpm.camunda.connector.file.api.impl.model.RequestData;

import java.io.IOException;

public interface ProcessFileCommand {

    RequestData uploadFile(RequestData request) throws IOException;

    RequestData deleteFile(RequestData request) throws IOException;

    RequestData downloadFile(RequestData request) throws IOException;

}
