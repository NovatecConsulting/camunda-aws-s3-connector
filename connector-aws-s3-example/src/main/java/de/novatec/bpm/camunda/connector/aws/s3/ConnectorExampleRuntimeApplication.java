package de.novatec.bpm.camunda.connector.aws.s3;

import io.camunda.connector.runtime.InboundConnectorsAutoConfiguration;
import io.camunda.zeebe.spring.client.annotation.Deployment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// we don't want the connector runtime to connect to Operate as it is
// not needed for outbound connectors
@SpringBootApplication(exclude = {InboundConnectorsAutoConfiguration.class})
@Deployment(resources = "bpmn/example.bpmn")
public class ConnectorExampleRuntimeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConnectorExampleRuntimeApplication.class, args);
    }
}
