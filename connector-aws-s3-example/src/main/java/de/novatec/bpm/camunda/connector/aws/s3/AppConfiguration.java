package de.novatec.bpm.camunda.connector.aws.s3;

import de.novatec.bpm.camunda.connector.aws.s3.adapter.in.process.MyJobWorker;
import de.novatec.bpm.camunda.connector.file.api.LocalFileCommand;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    MyJobWorker jobWorker(LocalFileCommand localFileCommand) {
        return new MyJobWorker(localFileCommand);
    }

}
