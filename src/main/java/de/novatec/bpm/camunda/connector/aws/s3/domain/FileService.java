package de.novatec.bpm.camunda.connector.aws.s3.domain;

import de.novatec.bpm.camunda.connector.aws.s3.domain.model.RequestData;
import de.novatec.bpm.camunda.connector.aws.s3.usecase.in.FileCommand;
import de.novatec.bpm.camunda.connector.aws.s3.usecase.out.S3Command;

import java.io.IOException;

public class FileService implements FileCommand {

    private final S3Command s3Command;

    public FileService(S3Command s3Command) {
        this.s3Command = s3Command;
    }

    public RequestData writeFile(RequestData request) throws IOException {
        s3Command.putObject(request);
        return request;
    }

    public RequestData deleteFile(RequestData request) {
        s3Command.deleteObject(request);
        return request;
    }

    public RequestData loadFile(RequestData request) throws IOException {
        byte[] content = s3Command.getObject(request);
        // writeFileToDisk(content);
        return request;
    }
}
