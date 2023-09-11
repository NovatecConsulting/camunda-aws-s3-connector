package de.novatec.bpm.camunda.connector.aws.s3.adapter.in.process;

import de.novatec.bpm.camunda.connector.aws.s3.adapter.in.process.model.ConnectorResponse;
import de.novatec.bpm.camunda.connector.aws.s3.domain.model.RequestData;
import de.novatec.bpm.camunda.connector.aws.s3.usecase.in.FileCommand;
import io.camunda.connector.api.annotation.OutboundConnector;
import io.camunda.connector.api.outbound.OutboundConnectorContext;
import io.camunda.connector.api.outbound.OutboundConnectorFunction;
import de.novatec.bpm.camunda.connector.aws.s3.adapter.in.process.model.ConnectorRequest;
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
    private FileCommand fileCommand;

    public ConnectorAdapter() {
    }

    public ConnectorAdapter(FileCommand fileCommand) {
        this.fileCommand = fileCommand;
    }

    @Override
    public Object execute(OutboundConnectorContext context) throws IOException {
        ConnectorRequest request = context.bindVariables(ConnectorRequest.class);
        logger.info("Executing connector with request {}", request);
        return execute(request);
    }

    private ConnectorResponse execute(ConnectorRequest request) throws IOException {
        RequestData requestData = RequestMapper.mapRequest(request);
        RequestData result = switch (request.getRequestDetails().getOperationType()) {
            case DELETE_OBJECT -> fileCommand.deleteFile(requestData);
            case PUT_OBJECT -> fileCommand.uploadFile(requestData);
            case GET_OBJECT -> fileCommand.downloadFile(requestData);
        };
        return new ConnectorResponse(result);
    }

}
