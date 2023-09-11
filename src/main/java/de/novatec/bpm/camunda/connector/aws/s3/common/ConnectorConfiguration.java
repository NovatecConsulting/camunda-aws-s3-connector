package de.novatec.bpm.camunda.connector.aws.s3.common;

import de.novatec.bpm.camunda.connector.aws.s3.adapter.out.FileAdapter;
import de.novatec.bpm.camunda.connector.aws.s3.adapter.out.S3ClientFactory;
import de.novatec.bpm.camunda.connector.aws.s3.domain.CloudFileService;
import de.novatec.bpm.camunda.connector.aws.s3.usecase.in.CloudFileCommand;
import de.novatec.bpm.camunda.connector.aws.s3.usecase.out.LocalFileCommand;
import de.novatec.bpm.camunda.connector.aws.s3.usecase.out.S3Command;
import de.novatec.bpm.camunda.connector.aws.s3.adapter.out.S3Adapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConnectorConfiguration {

    @Bean
    S3Command s3Command(S3ClientFactory clientFactory) {
        return new S3Adapter(clientFactory);
    }

    @Bean
    LocalFileCommand localFileCommand() {
        return new FileAdapter();
    }

    @Bean
    S3ClientFactory clientFactory() {
        return new S3ClientFactory();
    }

    @Bean
    CloudFileCommand cloudFileCommand(S3Command s3Command, LocalFileCommand localFileComand) {
        return new CloudFileService(s3Command, localFileComand);
    }
}
