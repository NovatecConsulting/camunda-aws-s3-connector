package info.novatec.bpm.camunda.connector.aws.s3;

import io.camunda.process.test.api.CamundaSpringProcessTest;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@SpringBootTest(classes = ExampleProcessApplication.class)
@CamundaSpringProcessTest
@Testcontainers
class ExampleProcessITTest {

    public static final String REPORT_BUCKET = "my-bucket";
    public static final String REPORT_KEY = "my-report.txt";
    static DockerImageName localstackImage = DockerImageName.parse("localstack/localstack:0.11.3");

    @Container
    static LocalStackContainer s3Service = new LocalStackContainer(localstackImage)
            .withServices(LocalStackContainer.Service.S3);

    @Autowired
    private ZeebeClient zeebe;

    private S3Client s3Client;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("aws.endpoint.override", s3Service::getEndpoint);
    }

    @BeforeAll
    static void start() {
        s3Service.start();
        System.setProperty("AWS_ACCESS_KEY", s3Service.getAccessKey());
        System.setProperty("AWS_SECRET_KEY", s3Service.getSecretKey());
    }

    @BeforeEach
    void setup_s3() {
        s3Client = S3Client
                .builder()
                .endpointOverride(s3Service.getEndpoint())
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(s3Service.getAccessKey(), s3Service.getSecretKey())
                        )
                )
                .region(Region.of(s3Service.getRegion()))
                .build();

        createBucket();
        uploadReport();
    }

    @Test
    void test_happy_path() throws IOException {

        ProcessInstanceEvent processInstance = zeebe.newCreateInstanceCommand()
                .bpmnProcessId("File_handling_process")
                .latestVersion()
                .variables("{ \"report\": { \"region\":\"" + s3Service.getRegion() + "\", \"bucket\":\"" + REPORT_BUCKET + "\", \"key\":\"" + REPORT_KEY + "\" } }")
                .send()
                .join();

        byte[] result = getResult(String.format("results/%d/my-file.txt", processInstance.getProcessInstanceKey()));
        Assertions.assertThat(result).isEqualTo("This is some random file".getBytes(StandardCharsets.UTF_8));
    }

    private byte[] getResult(String key) throws IOException {
        return s3Client.getObject(
                GetObjectRequest.builder()
                        .bucket(ExampleProcessITTest.REPORT_BUCKET)
                        .key(key)
                        .build()
        ).readAllBytes();
    }

    private void uploadReport() {
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(ExampleProcessITTest.REPORT_BUCKET)
                        .contentType("plain/text")
                        .key(ExampleProcessITTest.REPORT_KEY)
                        .build(),
                RequestBody.fromBytes("text".getBytes(StandardCharsets.UTF_8))
        );
    }

    private void createBucket() {
        s3Client.createBucket(CreateBucketRequest.builder()
                .bucket(ExampleProcessITTest.REPORT_BUCKET)
                .build()
        );
    }
}