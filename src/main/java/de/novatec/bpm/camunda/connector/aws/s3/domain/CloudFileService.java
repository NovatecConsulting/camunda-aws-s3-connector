package de.novatec.bpm.camunda.connector.aws.s3.domain;

import de.novatec.bpm.camunda.connector.aws.s3.domain.model.FileContent;
import de.novatec.bpm.camunda.connector.aws.s3.domain.model.RequestData;
import de.novatec.bpm.camunda.connector.aws.s3.usecase.in.FileCommand;
import de.novatec.bpm.camunda.connector.aws.s3.usecase.out.CloudFileCommand;
import de.novatec.bpm.camunda.connector.aws.s3.usecase.out.LocalFileCommand;

import java.io.IOException;
import java.util.Objects;

public class CloudFileService implements FileCommand {

    private final CloudFileCommand cloudFileCommand;
    private final LocalFileCommand localFileCommand;

    public CloudFileService(CloudFileCommand cloudFileCommand, LocalFileCommand localFileCommand) {
        this.cloudFileCommand = cloudFileCommand;
        this.localFileCommand = localFileCommand;
    }

    public RequestData uploadFile(RequestData request) throws IOException {
        String contentType = Objects.requireNonNull(request.getContentType(), "Content type variable is required for operation.");
        byte[] content = localFileCommand.loadFile(request.getFilePath());
        cloudFileCommand.putObject(request, FileContent.builder()
                .content(content)
                .contentLength((long) content.length)
                .contentType(contentType)
                .build()
        );
        return request;
    }

    public RequestData deleteFile(RequestData request) throws IOException {
        localFileCommand.deleteFile(request.getFilePath());
        cloudFileCommand.deleteObject(request);
        return request;
    }

    public RequestData downloadFile(RequestData request) throws IOException {
        FileContent response = cloudFileCommand.getObject(request);
        localFileCommand.saveFile(response.getContent(), request.getFilePath());
        return request;
    }
}
