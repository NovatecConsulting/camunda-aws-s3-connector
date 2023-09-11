package de.novatec.bpm.camunda.connector.aws.s3.adapter.out;

import de.novatec.bpm.camunda.connector.aws.s3.usecase.out.LocalFileCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileAdapter implements LocalFileCommand {

    Logger logger = LoggerFactory.getLogger(FileAdapter.class);

    public Path saveFile(byte[] content, String filePath) throws IOException {
        Path file = Path.of(filePath);
        logger.info("Writing file to {}", filePath);
        try (OutputStream stream = Files.newOutputStream(file)) {
            stream.write(content);
            logger.debug("{} bytes written to disk", content.length);
            return file;
        }
    }

    public byte[] loadFile(String filePath) throws IOException {
        logger.info("Reading file from {}", filePath);
        try (InputStream stream = Files.newInputStream(Path.of(filePath))) {
            byte[] bytes = stream.readAllBytes();
            logger.debug("{} bytes read from disk", bytes.length);
            return bytes;
        }
    }

    @Override
    public void deleteFile(String filePath) throws IOException {
        Path file = Path.of(filePath);
        logger.info("Deleting file {}", filePath);
        Files.delete(file);
        logger.debug("File deleted from disk");
    }

}
