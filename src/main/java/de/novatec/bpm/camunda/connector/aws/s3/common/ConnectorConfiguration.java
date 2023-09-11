package de.novatec.bpm.camunda.connector.aws.s3.common;

import de.novatec.bpm.camunda.connector.aws.s3.adapter.out.local.LocalFileAdapter;
import de.novatec.bpm.camunda.connector.aws.s3.adapter.out.cloud.CloudClientFactory;
import de.novatec.bpm.camunda.connector.aws.s3.domain.FileService;
import de.novatec.bpm.camunda.connector.aws.s3.usecase.in.FileCommand;
import de.novatec.bpm.camunda.connector.aws.s3.usecase.out.LocalFileCommand;
import de.novatec.bpm.camunda.connector.aws.s3.usecase.out.CloudFileCommand;
import de.novatec.bpm.camunda.connector.aws.s3.adapter.out.cloud.CloudFileAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;

@Configuration
public class ConnectorConfiguration {

    @Bean
    CloudFileCommand s3Command(CloudClientFactory clientFactory) {
        return new CloudFileAdapter(clientFactory);
    }

    @Bean
    LocalFileCommand localFileCommand() throws IOException {
        return new LocalFileAdapter(Files.createTempDirectory("aws-connector-files-"));
    }

    @Bean
    CloudClientFactory cloudClientFactory() {
        return new CloudClientFactory();
    }

    @Bean
    FileCommand fileCommand(CloudFileCommand cloudFileCommand, LocalFileCommand localFileCommand) {
        return new FileService(cloudFileCommand, localFileCommand);
    }
}
