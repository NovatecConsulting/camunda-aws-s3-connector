## Spring Boot Starter AWS S3 Connector

### Overview

This project is a Spring Boot starter designed to integrate with Spring Camunda projects for handling file operations through AWS S3 and local storage.
It provides essential beans and configurations for managing files in business processes, leveraging both cloud and local storage solutions.

## Configuration


    AWS Endpoint Override: This can be configured via the application.properties or application.yml file to 
    specify a custom endpoint, especially useful for testing with local S3 alternatives like LocalStack.

     properties: aws.endpoint.override=http://localhost:4566

## Key Beans and Services

    RemoteFileCommand: Handles operations with AWS S3.

    LocalFileCommand: Manages local filesystem interactions.

    CloudClientFactory: Creates instances of cloud clients, configurable with custom endpoints.

    ProcessFileCommand: A service that uses both remote and local file commands to perform comprehensive file operations.

    ConnectorAdapter: Integrates these file services with Camunda BPM processes, making them accessible in BPMN workflows.