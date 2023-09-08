package de.novatec.bpm.camunda.connector.aws.s3.common;

import de.novatec.bpm.camunda.connector.aws.s3.adapter.out.S3ClientFactory;
import de.novatec.bpm.camunda.connector.aws.s3.domain.FileService;
import de.novatec.bpm.camunda.connector.aws.s3.usecase.in.FileCommand;
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
    S3ClientFactory clientFactory() {
        return new S3ClientFactory();
    }

    @Bean
    FileCommand fileCommand(S3Command s3Command) {
        return new FileService(s3Command);
    }
}
