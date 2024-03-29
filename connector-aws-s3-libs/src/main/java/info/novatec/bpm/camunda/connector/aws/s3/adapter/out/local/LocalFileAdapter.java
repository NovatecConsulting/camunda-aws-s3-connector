package info.novatec.bpm.camunda.connector.aws.s3.adapter.out.local;

import info.novatec.bpm.camunda.connector.file.api.LocalFileCommand;
import info.novatec.bpm.camunda.connector.file.api.impl.exceptions.LocalFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class LocalFileAdapter implements LocalFileCommand {

    private final Path baseDir;
    Logger logger = LoggerFactory.getLogger(LocalFileAdapter.class);

    public LocalFileAdapter(Path baseDir) {
        this.baseDir = baseDir;
        logger.info("Initialized local file adapter base path: {}", baseDir);
    }

    public Path saveFile(byte[] content, String filePath) throws IOException {
        Path file = baseDir.resolve(filePath);
        if (Files.exists(file)) {
            throw new LocalFileException(String.format("The file already exists: %s", filePath));
        }
        Path directories = Files.createDirectories(file.getParent());
        logger.info("Created directories {}", directories);
        logger.info("Writing file to {}", filePath);
        try (OutputStream stream = Files.newOutputStream(file)) {
            stream.write(content);
            logger.debug("{} bytes written to disk", content.length);
            return file;
        }
    }

    public byte[] loadFile(String filePath) throws IOException {
        Path file = baseDir.resolve(filePath);
        if (!Files.exists(file)) {
            throw new LocalFileException(String.format("The file doesn't exist: %s", filePath));
        }
        logger.info("Reading file from {}", filePath);
        try (InputStream stream = Files.newInputStream(file)) {
            byte[] bytes = stream.readAllBytes();
            logger.debug("{} bytes read from disk", bytes.length);
            return bytes;
        }
    }

    @Override
    public void deleteFile(String filePath) throws IOException {
        Path file = baseDir.resolve(filePath);
        logger.info("Deleting file {}", filePath);
        boolean deleted = Files.deleteIfExists(file);
        if (deleted) {
            logger.debug("File deleted from disk: {}", filePath);
        } else {
            logger.debug("File didn't exist: {}", filePath);
        }
    }

}
