package de.novatec.bpm.camunda.connector.aws.s3;

import de.novatec.bpm.camunda.connector.aws.s3.adapter.in.process.ConnectorAdapter;
import de.novatec.bpm.camunda.connector.aws.s3.adapter.out.cloud.AmazonS3ClientFactory;
import de.novatec.bpm.camunda.connector.aws.s3.adapter.out.cloud.AmazonS3Adapter;
import de.novatec.bpm.camunda.connector.aws.s3.adapter.out.local.LocalFileAdapter;
import de.novatec.bpm.camunda.connector.file.api.RemoteFileCommand;
import de.novatec.bpm.camunda.connector.file.api.ProcessFileCommand;
import de.novatec.bpm.camunda.connector.file.api.LocalFileCommand;
import de.novatec.bpm.camunda.connector.file.api.impl.ProcessFileService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;

@Configuration
public class ConnectorStandaloneConfiguration {

    @Bean
    RemoteFileCommand remoteFileCommand(AmazonS3ClientFactory clientFactory) {
        return new AmazonS3Adapter(clientFactory);
    }

    @Bean
    LocalFileCommand localFileCommand() throws IOException {
        return new LocalFileAdapter(Files.createTempDirectory("connector-files-"));
    }

    @Bean
    AmazonS3ClientFactory cloudClientFactory() {
        return new AmazonS3ClientFactory();
    }

    @Bean
    ProcessFileCommand processFileCommand(RemoteFileCommand remoteFileCommand, LocalFileCommand localFileCommand) {
        return new ProcessFileService(remoteFileCommand, localFileCommand);
    }

    @Bean
    ConnectorAdapter connectorAdapter(ProcessFileCommand processFileCommand) {
        return new ConnectorAdapter(processFileCommand);
    }
}
