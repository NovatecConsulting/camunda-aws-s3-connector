package de.novatec.bpm.camunda.connector.aws.s3;

import de.novatec.bpm.camunda.connector.aws.s3.model.AuthenticationRequestData;
import de.novatec.bpm.camunda.connector.aws.s3.model.ConnectorRequest;
import de.novatec.bpm.camunda.connector.aws.s3.model.OperationType;
import de.novatec.bpm.camunda.connector.aws.s3.model.RequestDetails;
import io.camunda.connector.impl.ConnectorInputException;
import io.camunda.connector.test.outbound.OutboundConnectorContextBuilder;
import io.camunda.connector.test.outbound.OutboundConnectorContextBuilder.TestConnectorContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class S3ConnectorFunctionContextTest {

    @Test
    void should_replace_secrets() {
        // given
        ConnectorRequest request = new ConnectorRequest();
        request.setAuthentication(getAuthentication());

        TestConnectorContext context = OutboundConnectorContextBuilder.create()
                .secret("AWS_ACCESS_KEY", "abc")
                .secret("AWS_SECRET_KEY", "123")
                .build();

        // when
        context.replaceSecrets(request);

        // then
        assertThat(request).extracting("authentication")
                .extracting("accessKey")
                .as("access key")
                .isEqualTo("abc");

        assertThat(request).extracting("authentication")
                .extracting("secretKey")
                .as("secret key")
                .isEqualTo("123");
    }

    @Test
    void should_fail_if_authentication_is_missing() {
        // setup
        ConnectorRequest request = new ConnectorRequest();
        request.setRequestDetails(getDetails());
        TestConnectorContext context = OutboundConnectorContextBuilder.create().build();

        // expect
        assertThatThrownBy(() -> context.validate(request))
                .isInstanceOf(ConnectorInputException.class)
                .hasMessage("javax.validation.ValidationException: Found constraints violated while validating input: \n - authentication: must not be null");
    }

    @Test
    void should_fail_if_details_are_missing() {
        // setup
        ConnectorRequest request = new ConnectorRequest();
        AuthenticationRequestData authentication = getAuthentication();
        request.setAuthentication(authentication);
        TestConnectorContext context = OutboundConnectorContextBuilder.create().build();

        // expect
        assertThatThrownBy(() -> context.validate(request))
                .isInstanceOf(ConnectorInputException.class)
                .hasMessage("javax.validation.ValidationException: Found constraints violated while validating input: \n - requestDetails: must not be null");
    }

    @Test
    void should_fail_if_required_details_values_are_missing() {
        // setup
        ConnectorRequest request = new ConnectorRequest();
        request.setAuthentication(getAuthentication());
        request.setRequestDetails(new RequestDetails());
        var context = OutboundConnectorContextBuilder.create().build();

        // expect
        assertThatThrownBy(() -> context.validate(request))
                .isInstanceOf(ConnectorInputException.class)
                .hasMessageContainingAll("requestDetails.bucketName", "requestDetails.region", "requestDetails.objectKey", "requestDetails.operationType")
                .hasMessageNotContainingAny("requestDetails.filePath", "requestDetails.contentType");

    }

    @Test
    void should_fail_if_required_authentication_value_are_missing() {
        // setup
        ConnectorRequest request = new ConnectorRequest();
        request.setAuthentication(new AuthenticationRequestData());
        request.setRequestDetails(getDetails());
        var context = OutboundConnectorContextBuilder.create().build();

        // expect
        assertThatThrownBy(() -> context.validate(request))
                .isInstanceOf(ConnectorInputException.class)
                .hasMessageContainingAll("authentication.secretKey", "authentication.accessKey");

    }

    private RequestDetails getDetails() {
        RequestDetails details = new RequestDetails();
        details.setBucketName("bucket");
        details.setContentType("application/text");
        details.setFilePath("/tmp/invoice.txt");
        details.setObjectKey("/invoice/invoice-123.txt");
        details.setOperationType(OperationType.PUT_OBJECT);
        details.setRegion("eu-central-1");
        return details;
    }

    private AuthenticationRequestData getAuthentication() {
        AuthenticationRequestData authentication = new AuthenticationRequestData();
        authentication.setAccessKey("secrets.AWS_ACCESS_KEY");
        authentication.setSecretKey("secrets.AWS_SECRET_KEY");
        return authentication;
    }

}