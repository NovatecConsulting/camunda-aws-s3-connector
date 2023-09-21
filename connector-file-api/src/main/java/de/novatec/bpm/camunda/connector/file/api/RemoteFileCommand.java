package de.novatec.bpm.camunda.connector.file.api;

import de.novatec.bpm.camunda.connector.file.api.impl.model.FileContent;
import de.novatec.bpm.camunda.connector.file.api.impl.model.RequestData;

import java.io.IOException;

public interface RemoteFileCommand {
    void deleteFile(RequestData requestData);
    void putFile(RequestData requestData, FileContent fileContent) throws IOException;
    FileContent getFile(RequestData requestData) throws IOException;
}
