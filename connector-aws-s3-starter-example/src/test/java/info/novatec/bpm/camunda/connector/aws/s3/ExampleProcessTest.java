package info.novatec.bpm.camunda.connector.aws.s3;

import info.novatec.bpm.camunda.connector.file.api.LocalFileCommand;
import info.novatec.bpm.camunda.connector.file.api.RemoteFileCommand;
import info.novatec.bpm.camunda.connector.file.api.impl.model.FileContent;
import info.novatec.bpm.camunda.connector.file.api.impl.model.RequestData;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import io.camunda.process.test.api.CamundaSpringProcessTest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static io.camunda.process.test.api.CamundaAssert.assertThat;

@SpringBootTest(classes = ExampleProcessApplication.class)
@CamundaSpringProcessTest
class ExampleProcessTest {

    @Autowired
    private ZeebeClient zeebe;

    @MockBean
    private LocalFileCommand localFileCommand;

    @MockBean(name = "remoteFileCommand")
    private RemoteFileCommand remoteFileCommand;

    @BeforeAll
    static void start() {
        System.setProperty("AWS_ACCESS_KEY", "accessKey");
        System.setProperty("AWS_SECRET_KEY", "secretKey");
    }

    @Test
    void test_happy_path() throws IOException {

        when(localFileCommand.loadFile(anyString())).thenReturn("anythingLocal".getBytes(StandardCharsets.UTF_8));
        when(remoteFileCommand.getFile(any(RequestData.class))).thenReturn(
                FileContent.builder()
                        .content("anythingRemote".getBytes(StandardCharsets.UTF_8))
                        .contentLength(14L)
                        .contentType("plain/text")
                        .build()
        );

        ProcessInstanceEvent processInstance = zeebe.newCreateInstanceCommand()
                .bpmnProcessId("File_handling_process")
                .latestVersion()
                .variables("{ \"report\": { \"region\":\"region\", \"bucket\":\"bucket\", \"key\":\"key\" } }")
                .send()
                .join();

        assertThat(processInstance).isActive();
        assertThat(processInstance)
                .hasCompletedElements("add_context", "download_file",
                        "validate_file", "generate_result", "upload_file", "file_valid");
        assertThat(processInstance)
                .hasTerminatedElements("delete_file","file_invalid" );
        assertThat(processInstance).isCompleted();

        // for remote download
        verify(remoteFileCommand).getFile(any(RequestData.class));
        // for remote upload
        verify(remoteFileCommand).putFile(any(RequestData.class), any());
        // after download and after file generation
        verify(localFileCommand, times(2)).saveFile(any(), anyString());
        // for validation, for generation, before upload
        verify(localFileCommand, times(3)).loadFile(anyString());

        verifyNoMoreInteractions(localFileCommand, remoteFileCommand);
    }

    @Test
    void test_deletion() throws IOException {

        when(localFileCommand.loadFile(anyString())).thenReturn("".getBytes(StandardCharsets.UTF_8));
        when(remoteFileCommand.getFile(any(RequestData.class))).thenReturn(
                FileContent.builder()
                        .content("".getBytes(StandardCharsets.UTF_8))
                        .contentLength(0L)
                        .contentType("plain/text")
                        .build()
        );

        ProcessInstanceEvent processInstance = zeebe.newCreateInstanceCommand()
                .bpmnProcessId("File_handling_process")
                .latestVersion()
                .variables("{ \"report\": { \"region\":\"region\", \"bucket\":\"bucket\", \"key\":\"key\" } }")
                .send()
                .join();

        assertThat(processInstance).isActive();
        assertThat(processInstance)
                .hasCompletedElements("add_context", "download_file",
                        "validate_file", "delete_file", "file_valid");
        assertThat(processInstance)
                .hasTerminatedElements("generate_result", "upload_file", "file_valid");
        assertThat(processInstance).isCompleted();

        // for remote download
        verify(remoteFileCommand).getFile(any(RequestData.class));
        // for remote deletion
        verify(remoteFileCommand).deleteFile(any(RequestData.class));
        // after remote download
        verify(localFileCommand).saveFile(any(), anyString());
        // for validation
        verify(localFileCommand).loadFile(anyString());
        // after remote deletion
        verify(localFileCommand).deleteFile(anyString());

        verifyNoMoreInteractions(localFileCommand, remoteFileCommand);
    }
}