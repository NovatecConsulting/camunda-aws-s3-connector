package de.novatec.bpm.camunda.connector.file.api;

import de.novatec.bpm.camunda.connector.file.api.impl.model.FileContent;
import de.novatec.bpm.camunda.connector.file.api.impl.model.RequestData;

import java.io.IOException;

public interface RemoteFileCommand {
    void deleteObject(RequestData requestData);
    void putObject(RequestData requestData, FileContent fileContent) throws IOException;
    FileContent getObject(RequestData requestData) throws IOException;
}
