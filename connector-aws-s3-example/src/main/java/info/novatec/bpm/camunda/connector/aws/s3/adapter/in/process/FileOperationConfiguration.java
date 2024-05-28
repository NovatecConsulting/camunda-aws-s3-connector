package info.novatec.bpm.camunda.connector.aws.s3.adapter.in.process;

import info.novatec.bpm.camunda.connector.aws.s3.adapter.out.cloud.CloudClientFactory;
import info.novatec.bpm.camunda.connector.file.api.LocalFileCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileOperationConfiguration {

    @Bean
    FileOperationAdapter jobWorker(LocalFileCommand localFileCommand) {
        return new FileOperationAdapter(localFileCommand);
    }

}
