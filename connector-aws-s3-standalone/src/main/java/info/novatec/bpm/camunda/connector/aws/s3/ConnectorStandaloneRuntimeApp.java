package info.novatec.bpm.camunda.connector.aws.s3;

import io.camunda.connector.runtime.InboundConnectorsAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = InboundConnectorsAutoConfiguration.class)
public class ConnectorStandaloneRuntimeApp {
    public static void main(String[] args) {
        SpringApplication.run(ConnectorStandaloneRuntimeApp.class, args);
    }
}
