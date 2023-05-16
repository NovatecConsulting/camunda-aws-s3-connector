package de.novatec.bpm.camunda.connector.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import de.novatec.bpm.camunda.connector.aws.s3.model.*;
import de.novatec.bpm.camunda.connector.aws.s3.service.S3Service;
import de.novatec.bpm.camunda.connector.aws.s3.service.S3ServiceFactory;
import io.camunda.connector.test.outbound.OutboundConnectorContextBuilder;
import io.camunda.connector.test.outbound.OutboundConnectorContextBuilder.TestConnectorContext;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class S3ConnectorFunctionTest {

    @Test
    void happy_path_aws_put_is_called_as_expected() throws IOException {
        // given
        AmazonS3 client = Mockito.mock(AmazonS3.class);
        S3Service s3Service = S3ServiceFactory.getService(client);
        PutObjectResult awsResult = createPutResult();
        when(client.putObject(any())).thenReturn(awsResult);

        S3ConnectorFunction function = new S3ConnectorFunction(s3Service);

        URL resource = getClass().getClassLoader().getResource("invoices/invoice.txt");
        byte[] fileBytes = getFileBytes(resource);

        ConnectorRequest request = new ConnectorRequest();
        request.setAuthentication(getAuthentication());
        request.setRequestDetails(getDetails(Objects.requireNonNull(resource).getPath()));

        TestConnectorContext context = OutboundConnectorContextBuilder.create()
                .secret("AWS_ACCESS_KEY", "abc")
                .secret("AWS_SECRET_KEY", "123")
                .variables(request)
                .build();

        // when
        ConnectorResponse actualResult = (ConnectorResponse) function.execute(context);

        // then
        assertThat(actualResult).isEqualTo(new ConnectorResponse(awsResult));
        ArgumentCaptor<PutObjectRequest> argumentCaptor = ArgumentCaptor.forClass(PutObjectRequest.class);
        verify(client, times(1)).putObject(argumentCaptor.capture());
        PutObjectRequest putRequest = argumentCaptor.getValue();
        // content + target
        assertThat(putRequest.getBucketName()).isEqualTo("bucket");
        assertThat(putRequest.getKey()).isEqualTo("invoice/invoice-123.txt");
        assertThat(putRequest.getInputStream().readAllBytes()).isEqualTo(fileBytes);
        // metadata
        assertThat(putRequest.getMetadata().getContentType()).isEqualTo("application/text");
        assertThat(putRequest.getMetadata().getSSEAlgorithm()).isEqualTo(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);
        assertThat(putRequest.getMetadata().getContentLength()).isEqualTo(fileBytes.length);

        verifyNoMoreInteractions(client);
    }

    private static PutObjectResult createPutResult() {
        PutObjectResult awsResult = new PutObjectResult();
        awsResult.setContentMd5("md5");
        awsResult.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);
        awsResult.setVersionId("1234567");
        return awsResult;
    }

    private static byte[] getFileBytes(URL resource) throws IOException {
        try (var fis = new FileInputStream(resource.getPath())) {
            return fis.readAllBytes();
        }
    }

    private RequestDetails getDetails(String path) {
        RequestDetails details = new RequestDetails();
        details.setBucketName("bucket");
        details.setObjectKey("invoice/invoice-123.txt");
        details.setContentType("application/text");
        details.setFilePath(path);
        details.setOperationType(OperationType.upload);
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