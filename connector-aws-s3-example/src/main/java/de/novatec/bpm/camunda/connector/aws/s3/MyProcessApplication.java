package de.novatec.bpm.camunda.connector.aws.s3;

import io.camunda.zeebe.spring.client.annotation.Deployment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Deployment(resources = "bpmn/example.bpmn")
public class MyProcessApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyProcessApplication.class, args);
    }
}
