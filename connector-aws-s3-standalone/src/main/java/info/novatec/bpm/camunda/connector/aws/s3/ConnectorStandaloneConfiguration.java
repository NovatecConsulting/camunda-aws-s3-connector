package info.novatec.bpm.camunda.connector.aws.s3;

import info.novatec.bpm.camunda.connector.aws.s3.adapter.in.process.ConnectorAdapter;
import info.novatec.bpm.camunda.connector.aws.s3.adapter.out.cloud.CloudClientFactory;
import info.novatec.bpm.camunda.connector.aws.s3.adapter.out.cloud.CloudFileAdapter;
import info.novatec.bpm.camunda.connector.aws.s3.adapter.out.local.LocalFileAdapter;
import info.novatec.bpm.camunda.connector.file.api.RemoteFileCommand;
import info.novatec.bpm.camunda.connector.file.api.ProcessFileCommand;
import info.novatec.bpm.camunda.connector.file.api.LocalFileCommand;
import info.novatec.bpm.camunda.connector.file.api.impl.ProcessFileService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;

@Configuration
public class ConnectorStandaloneConfiguration {

    @Bean
    RemoteFileCommand remoteFileCommand(CloudClientFactory clientFactory) {
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
    ProcessFileCommand processFileCommand(RemoteFileCommand remoteFileCommand, LocalFileCommand localFileCommand) {
        return new ProcessFileService(remoteFileCommand, localFileCommand);
    }

    @Bean
    ConnectorAdapter connectorAdapter(ProcessFileCommand processFileCommand) {
        return new ConnectorAdapter(processFileCommand);
    }
}
