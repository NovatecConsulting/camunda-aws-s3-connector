package de.novatec.bpm.camunda.connector.aws.s3.domain;

import de.novatec.bpm.camunda.connector.aws.s3.domain.model.FileContent;
import de.novatec.bpm.camunda.connector.aws.s3.domain.model.RequestData;
import de.novatec.bpm.camunda.connector.aws.s3.usecase.in.FileCommand;
import de.novatec.bpm.camunda.connector.aws.s3.usecase.out.LocalFileCommand;
import de.novatec.bpm.camunda.connector.aws.s3.usecase.out.CloudFileCommand;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public class FileService implements FileCommand {

    private final CloudFileCommand cloudFileCommand;
    private final LocalFileCommand localFileCommand;

    public FileService(CloudFileCommand cloudFileCommand, LocalFileCommand localFileCommand) {
        this.cloudFileCommand = cloudFileCommand;
        this.localFileCommand = localFileCommand;
    }

    public RequestData uploadFile(RequestData request) throws IOException {
        String filePath = Objects.requireNonNull(request.getFilePath(), "File path variable is required for operation.");
        String contentType = Objects.requireNonNull(request.getContentType(), "Content type variable is required for operation.");
        byte[] content = localFileCommand.loadFile(filePath);
        cloudFileCommand.putObject(request, FileContent.builder()
                .content(content)
                .contentLength((long) content.length)
                .contentType(contentType)
                .build()
        );
        return request;
    }

    public RequestData deleteFile(RequestData request) throws IOException {
        String filePath = Objects.requireNonNull(request.getFilePath(), "File path variable is required for operation.");
        localFileCommand.deleteFile(filePath);
        cloudFileCommand.deleteObject(request);
        return request;
    }

    public RequestData downloadFile(RequestData request) throws IOException {
        String filePath = Objects.requireNonNull(request.getFilePath(), "File path variable is required for operation.");
        FileContent response = cloudFileCommand.getObject(request);
        Path path = localFileCommand.saveFile(response.getContent(), filePath);
        request.setFilePath(path.toString());
        request.setContentType(response.getContentType());
        return request;
    }
}
