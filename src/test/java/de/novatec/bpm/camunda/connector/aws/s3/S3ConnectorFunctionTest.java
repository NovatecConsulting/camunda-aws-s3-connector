package de.novatec.bpm.camunda.connector.aws.s3;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.novatec.bpm.camunda.connector.aws.s3.model.*;
import de.novatec.bpm.camunda.connector.aws.s3.service.S3Service;
import de.novatec.bpm.camunda.connector.aws.s3.service.S3ServiceFactory;
import io.camunda.connector.test.outbound.OutboundConnectorContextBuilder;
import io.camunda.connector.test.outbound.OutboundConnectorContextBuilder.TestConnectorContext;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.ServerSideEncryption;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class S3ConnectorFunctionTest {

    @Test
    void happy_path_aws_put_is_called_as_expected() throws IOException {
        // given
        S3Client client = Mockito.mock(S3Client.class);
        S3Service s3Service = S3ServiceFactory.getService(client);
        PutObjectResponse awsResult = createPutResponse();
        when(client.putObject(any(PutObjectRequest.class), any(RequestBody.class))).thenReturn(awsResult);

        S3ConnectorFunction function = new S3ConnectorFunction(s3Service);

        URL resource = getClass().getClassLoader().getResource("invoices/invoice.txt");
        byte[] fileBytes = getFileBytes(resource);

        ConnectorRequest request = new ConnectorRequest();
        request.setAuthentication(getAuthentication());
        String filePath = Objects.requireNonNull(resource).getPath();
        request.setRequestDetails(getPutDetails("bucket", "path/file.txt", filePath));

        TestConnectorContext context = OutboundConnectorContextBuilder.create()
                .secret("AWS_ACCESS_KEY", "abc")
                .secret("AWS_SECRET_KEY", "123")
                .variables(new ObjectMapper().writeValueAsString(request))
                .build();

        // when
        ConnectorResponse actualResult = (ConnectorResponse) function.execute(context);

        // then
        assertThat(actualResult).isEqualTo(new ConnectorResponse(awsResult));

        ArgumentCaptor<PutObjectRequest> requestCaptor = ArgumentCaptor.forClass(PutObjectRequest.class);
        ArgumentCaptor<RequestBody> bodyCaptor = ArgumentCaptor.forClass(RequestBody.class);
        verify(client, times(1)).putObject(requestCaptor.capture(), bodyCaptor.capture());
        PutObjectRequest putRequest = requestCaptor.getValue();
        assertThat(putRequest.bucket()).isEqualTo("bucket");
        assertThat(putRequest.key()).isEqualTo("path/file.txt");
        assertThat(putRequest.contentLength()).isEqualTo(fileBytes.length);
        assertThat(putRequest.contentType()).isEqualTo("application/text");

        RequestBody requestBody = bodyCaptor.getValue();
        try(InputStream is = requestBody.contentStreamProvider().newStream()) {
            assertThat(is.readAllBytes()).isEqualTo(fileBytes);
        }

        verifyNoMoreInteractions(client);
    }

    @Test
    void happy_path_aws_delete_is_called_as_expected() throws IOException {
        // given
        S3Client client = Mockito.mock(S3Client.class);
        S3Service s3Service = S3ServiceFactory.getService(client);

        S3ConnectorFunction function = new S3ConnectorFunction(s3Service);


        ConnectorRequest request = new ConnectorRequest();
        request.setAuthentication(getAuthentication());
        request.setRequestDetails(getDeleteDetails("bucket", "path/file.txt"));

        TestConnectorContext context = OutboundConnectorContextBuilder.create()
                .secret("AWS_ACCESS_KEY", "abc")
                .secret("AWS_SECRET_KEY", "123")
                .variables(new ObjectMapper().writeValueAsString(request))
                .build();

        // when
        ConnectorResponse actualResult = (ConnectorResponse) function.execute(context);

        // then
        assertThat(actualResult).isEqualTo(new ConnectorResponse());

        ArgumentCaptor<DeleteObjectRequest> argumentCaptor = ArgumentCaptor.forClass(DeleteObjectRequest.class);
        verify(client, times(1)).deleteObject(argumentCaptor.capture());
        DeleteObjectRequest deleteRequest = argumentCaptor.getValue();
        assertThat(deleteRequest.bucket()).isEqualTo("bucket");
        assertThat(deleteRequest.key()).isEqualTo("path/file.txt");

        verifyNoMoreInteractions(client);
    }

    private static PutObjectResponse createPutResponse() {
         return PutObjectResponse.builder()
                .versionId("1234567")
                .serverSideEncryption(ServerSideEncryption.AES256)
                .checksumSHA256("foo")
                .build();
    }

    private static byte[] getFileBytes(URL resource) throws IOException {
        try (var fis = new FileInputStream(resource.getPath())) {
            return fis.readAllBytes();
        }
    }

    private RequestDetails getPutDetails(String bucket, String key, String path) {
        RequestDetails details = new RequestDetails();
        details.setBucketName(bucket);
        details.setObjectKey(key);
        details.setContentType("application/text");
        details.setFilePath(path);
        details.setOperationType(OperationType.PUT_OBJECT);
        details.setRegion("eu-central-1");
        return details;
    }

    private RequestDetails getDeleteDetails(String bucket, String key) {
        RequestDetails details = new RequestDetails();
        details.setBucketName(bucket);
        details.setObjectKey(key);
        details.setOperationType(OperationType.DELETE_OBJECT);
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