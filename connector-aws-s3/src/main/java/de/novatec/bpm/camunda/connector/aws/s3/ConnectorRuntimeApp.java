package de.novatec.bpm.camunda.connector.aws.s3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConnectorRuntimeApp {
    public static void main(String[] args) {
        SpringApplication.run(ConnectorRuntimeApp.class, args);
    }
}
