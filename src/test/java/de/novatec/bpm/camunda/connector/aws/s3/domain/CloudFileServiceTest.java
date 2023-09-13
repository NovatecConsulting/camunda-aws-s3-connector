package de.novatec.bpm.camunda.connector.aws.s3.domain;

import de.novatec.bpm.camunda.connector.aws.s3.domain.model.FileContent;
import de.novatec.bpm.camunda.connector.aws.s3.domain.model.RequestData;
import de.novatec.bpm.camunda.connector.aws.s3.usecase.out.CloudFileCommand;
import de.novatec.bpm.camunda.connector.aws.s3.usecase.out.LocalFileCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CloudFileServiceTest {

    @Mock
    CloudFileCommand cloudFileCommand;

    @Mock
    LocalFileCommand localFileCommand;

    private CloudFileService service;

    @BeforeEach
    void setUp() {
        service = new CloudFileService(cloudFileCommand, localFileCommand);
    }

    @Test
    void file_service_for_upload_called_as_expected() throws IOException {
        // given
        RequestData requestData = RequestData.builder()
                .filePath("myfile.txt")
                .contentType("contentType")
                .build();
        byte[] expectedContent = "foo".getBytes(StandardCharsets.UTF_8);
        when(localFileCommand.loadFile(anyString())).thenReturn(expectedContent);

        // when
        RequestData response = service.uploadFile(requestData);

        // then
        verify(localFileCommand, times(1)).loadFile(eq("myfile.txt"));
        ArgumentCaptor<FileContent> argumentCaptor = ArgumentCaptor.forClass(FileContent.class);
        verify(cloudFileCommand, times(1)).putObject(eq(requestData), argumentCaptor.capture());
        FileContent value = argumentCaptor.getValue();
        assertThat(value.getContent()).as("content").isEqualTo(expectedContent);
        assertThat(value.getContentLength()).as("content length").isEqualTo(expectedContent.length);
        assertThat(value.getContentType()).as("content type").isEqualTo("contentType");
        assertThat(response).isEqualTo(requestData);
    }

    @Test
    void file_service_for_delete_called_as_expected() throws IOException {
        // given
        RequestData requestData = RequestData.builder()
                .filePath("myfile.txt")
                .build();

        // when
        RequestData response = service.deleteFile(requestData);

        // then
        verify(localFileCommand, times(1)).deleteFile(eq("myfile.txt"));
        verify(cloudFileCommand, times(1)).deleteObject(eq(requestData));
        assertThat(response).isEqualTo(requestData);
    }

    @Test
    void file_service_for_download_called_as_expected() throws IOException {
        // given
        RequestData requestData = RequestData.builder()
                .filePath("myfile.txt")
                .contentType("contentType")
                .build();
        byte[] expectedContent = "foo".getBytes(StandardCharsets.UTF_8);
        when(cloudFileCommand.getObject(eq(requestData))).thenReturn(FileContent.builder().content(expectedContent).build());
        when(localFileCommand.saveFile(eq(expectedContent), eq("myfile.txt"))).thenReturn(Path.of("myfile.txt"));

        // when
        RequestData response = service.downloadFile(requestData);

        // then
        verify(cloudFileCommand, times(1)).getObject(eq(requestData));
        verify(localFileCommand, times(1)).saveFile(eq(expectedContent), eq("myfile.txt"));
        assertThat(response).isEqualTo(requestData);
    }
}