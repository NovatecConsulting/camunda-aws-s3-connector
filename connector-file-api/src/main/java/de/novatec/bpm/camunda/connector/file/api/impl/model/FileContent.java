package de.novatec.bpm.camunda.connector.file.api.impl.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileContent {

    private byte[] content;
    private String contentType;
    private Long contentLength;

}
