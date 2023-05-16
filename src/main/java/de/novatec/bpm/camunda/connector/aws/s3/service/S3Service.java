package de.novatec.bpm.camunda.connector.aws.s3.service;

import de.novatec.bpm.camunda.connector.aws.s3.model.ConnectorResponse;
import de.novatec.bpm.camunda.connector.aws.s3.model.RequestDetails;

import java.io.IOException;

public interface S3Service {
    ConnectorResponse deleteObject(RequestDetails details);
    ConnectorResponse putObject(RequestDetails details) throws IOException;
}
