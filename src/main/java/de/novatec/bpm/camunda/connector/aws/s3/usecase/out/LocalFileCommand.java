package de.novatec.bpm.camunda.connector.aws.s3.usecase.out;

import java.io.IOException;
import java.nio.file.Path;

public interface LocalFileCommand {

    Path saveFile(byte[] content, String filePath) throws IOException;

    byte[] loadFile(String filePath) throws IOException;

    void deleteFile(String filePath) throws IOException;

}
