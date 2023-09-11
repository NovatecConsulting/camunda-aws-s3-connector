package de.novatec.bpm.camunda.connector.aws.s3.domain;

import de.novatec.bpm.camunda.connector.aws.s3.domain.model.FileContent;
import de.novatec.bpm.camunda.connector.aws.s3.domain.model.S3RequestData;
import de.novatec.bpm.camunda.connector.aws.s3.usecase.in.CloudFileCommand;
import de.novatec.bpm.camunda.connector.aws.s3.usecase.out.LocalFileCommand;
import de.novatec.bpm.camunda.connector.aws.s3.usecase.out.S3Command;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public class CloudFileService implements CloudFileCommand {

    private final S3Command s3Command;
    private final LocalFileCommand localFileCommand;

    public CloudFileService(S3Command s3Command, LocalFileCommand localFileCommand) {
        this.s3Command = s3Command;
        this.localFileCommand = localFileCommand;
    }

    public S3RequestData uploadFile(S3RequestData request) throws IOException {
        String filePath = Objects.requireNonNull(request.getFilePath(), "File path variable is required for operation.");
        String contentType = Objects.requireNonNull(request.getContentType(), "Content type variable is required for operation.");
        byte[] content = localFileCommand.loadFile(filePath);
        s3Command.putObject(request, FileContent.builder()
                .content(content)
                .contentLength((long) content.length)
                .contentType(contentType)
                .build()
        );
        return request;
    }

    public S3RequestData deleteFile(S3RequestData request) throws IOException {
        String filePath = Objects.requireNonNull(request.getFilePath(), "File path variable is required for operation.");
        localFileCommand.deleteFile(filePath);
        s3Command.deleteObject(request);
        return request;
    }

    public S3RequestData downloadFile(S3RequestData request) throws IOException {
        String filePath = Objects.requireNonNull(request.getFilePath(), "File path variable is required for operation.");
        FileContent response = s3Command.getObject(request);
        Path path = localFileCommand.saveFile(response.getContent(), filePath);
        request.setFilePath(path.toString());
        request.setContentType(response.getContentType());
        return request;
    }
}
