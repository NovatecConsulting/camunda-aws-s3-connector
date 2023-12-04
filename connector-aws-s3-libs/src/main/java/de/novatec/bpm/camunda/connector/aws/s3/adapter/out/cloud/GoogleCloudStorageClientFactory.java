package de.novatec.bpm.camunda.connector.aws.s3.adapter.out.cloud;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GoogleCloudStorageClientFactory {

    private static final Logger logger = LoggerFactory.getLogger(GoogleCloudStorageClientFactory.class);

    public Storage createClient(String jsonFilePath) throws IOException {
        logger.info("Google cloud storage client initialized with config: {}", jsonFilePath);
        return StorageOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(Files.newInputStream(Path.of(jsonFilePath))))
                .build()
                .getService();
    }

}
