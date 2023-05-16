package de.novatec.bpm.camunda.connector.aws.s3;

import de.novatec.bpm.camunda.connector.aws.s3.service.S3Service;
import de.novatec.bpm.camunda.connector.aws.s3.service.S3ServiceFactory;
import io.camunda.connector.api.annotation.OutboundConnector;
import io.camunda.connector.api.outbound.OutboundConnectorContext;
import io.camunda.connector.api.outbound.OutboundConnectorFunction;
import de.novatec.bpm.camunda.connector.aws.s3.model.ConnectorRequest;
import de.novatec.bpm.camunda.connector.aws.s3.model.ConnectorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@OutboundConnector(
        name = "AWSS3",
        inputVariables = {"authentication", "requestDetails"},
        type = "de.novatec.bpm:aws-s3:1")
public class S3ConnectorFunction implements OutboundConnectorFunction {

    private static final Logger logger = LoggerFactory.getLogger(S3ConnectorFunction.class);
    private S3Service s3Service;

    public S3ConnectorFunction() {
    }

    public S3ConnectorFunction(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @Override
    public Object execute(OutboundConnectorContext context) throws IOException {
        var request = context.getVariablesAsType(ConnectorRequest.class);
        logger.info("Executing connector with request {}", request);
        context.validate(request);
        context.replaceSecrets(request);
        return execute(request);
    }

    private ConnectorResponse execute(ConnectorRequest request) throws IOException {
        if (s3Service == null) {
            s3Service = S3ServiceFactory.getService(request.getAuthentication(), request.getRequestDetails().getRegion());
        }
        return switch (request.getRequestDetails().getOperationType()) {
            case DELETE_OBJECT -> s3Service.deleteObject(request.getRequestDetails());
            case PUT_OBJECT -> s3Service.putObject(request.getRequestDetails());
        };
    }

}
