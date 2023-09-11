package de.novatec.bpm.camunda.connector.aws.s3.adapter.in;

import de.novatec.bpm.camunda.connector.aws.s3.adapter.in.model.ConnectorResponse;
import de.novatec.bpm.camunda.connector.aws.s3.domain.model.S3RequestData;
import de.novatec.bpm.camunda.connector.aws.s3.usecase.in.CloudFileCommand;
import io.camunda.connector.api.annotation.OutboundConnector;
import io.camunda.connector.api.outbound.OutboundConnectorContext;
import io.camunda.connector.api.outbound.OutboundConnectorFunction;
import de.novatec.bpm.camunda.connector.aws.s3.adapter.in.model.ConnectorRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@OutboundConnector(
        name = "AWSS3",
        inputVariables = {"authentication", "requestDetails"},
        type = "de.novatec.bpm:aws-s3:1")
public class ConnectorAdapter implements OutboundConnectorFunction {

    private static final Logger logger = LoggerFactory.getLogger(ConnectorAdapter.class);
    private CloudFileCommand cloudFileCommand;

    public ConnectorAdapter() {
    }

    public ConnectorAdapter(CloudFileCommand cloudFileCommand) {
        this.cloudFileCommand = cloudFileCommand;
    }

    @Override
    public Object execute(OutboundConnectorContext context) throws IOException {
        ConnectorRequest request = context.bindVariables(ConnectorRequest.class);
        logger.info("Executing connector with request {}", request);
        return execute(request);
    }

    private ConnectorResponse execute(ConnectorRequest request) throws IOException {
        S3RequestData s3RequestData = RequestMapper.mapRequest(request);
        S3RequestData result = switch (request.getRequestDetails().getOperationType()) {
            case DELETE_OBJECT -> cloudFileCommand.deleteFile(s3RequestData);
            case PUT_OBJECT -> cloudFileCommand.uploadFile(s3RequestData);
            case GET_OBJECT -> cloudFileCommand.downloadFile(s3RequestData);
        };
        return new ConnectorResponse(result);
    }

}
