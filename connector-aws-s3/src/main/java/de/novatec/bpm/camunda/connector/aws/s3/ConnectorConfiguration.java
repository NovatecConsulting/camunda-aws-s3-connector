package de.novatec.bpm.camunda.connector.aws.s3;

import de.novatec.bpm.camunda.connector.aws.s3.adapter.in.process.ConnectorAdapter;
import de.novatec.bpm.camunda.connector.aws.s3.adapter.out.cloud.CloudClientFactory;
import de.novatec.bpm.camunda.connector.aws.s3.adapter.out.cloud.CloudFileAdapter;
import de.novatec.bpm.camunda.connector.aws.s3.adapter.out.local.LocalFileAdapter;
import de.novatec.bpm.camunda.connector.file.api.CloudFileCommand;
import de.novatec.bpm.camunda.connector.file.api.FileCommand;
import de.novatec.bpm.camunda.connector.file.api.LocalFileCommand;
import de.novatec.bpm.camunda.connector.file.api.impl.CloudFileService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;

@Configuration
public class ConnectorConfiguration {

    @Bean
    CloudFileCommand cloudFileCommand(CloudClientFactory clientFactory) {
        return new CloudFileAdapter(clientFactory);
    }

    @Bean
    LocalFileCommand localFileCommand() throws IOException {
        return new LocalFileAdapter(Files.createTempDirectory("connector-files-"));
    }

    @Bean
    CloudClientFactory cloudClientFactory() {
        return new CloudClientFactory();
    }

    @Bean
    FileCommand fileCommand(CloudFileCommand cloudFileCommand, LocalFileCommand localFileCommand) {
        return new CloudFileService(cloudFileCommand, localFileCommand);
    }

    @Bean
    ConnectorAdapter connector(FileCommand fileCommand) {
        return new ConnectorAdapter(fileCommand);
    }
}
