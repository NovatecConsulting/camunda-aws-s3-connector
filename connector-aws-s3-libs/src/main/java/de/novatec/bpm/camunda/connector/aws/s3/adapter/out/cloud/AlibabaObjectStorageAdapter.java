package de.novatec.bpm.camunda.connector.aws.s3.adapter.out.cloud;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.*;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import de.novatec.bpm.camunda.connector.file.api.RemoteFileCommand;
import de.novatec.bpm.camunda.connector.file.api.impl.model.FileContent;
import de.novatec.bpm.camunda.connector.file.api.impl.model.RequestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class AlibabaObjectStorageAdapter implements RemoteFileCommand {

    private static final Logger logger = LoggerFactory.getLogger(AlibabaObjectStorageAdapter.class);

    private final AlibabaObjectStorageClientFactory clientFactory;

    public AlibabaObjectStorageAdapter(AlibabaObjectStorageClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    public void deleteFile(RequestData requestData) {
        OSS client = clientFactory.createClient(requestData.getAuthenticationKey(), requestData.getAuthenticationSecret());
        try {
            client.deleteObject(requestData.getBucket(), requestData.getKey());
            logger.info("File deleted: {}/{}", requestData.getBucket(), requestData.getKey());
        } finally {
            client.shutdown();
        }
    }

    @Override
    public void putFile(RequestData requestData, FileContent fileContent) {
        OSS client = clientFactory.createClient(requestData.getAuthenticationKey(), requestData.getAuthenticationSecret());
        UploadFileRequest request = new UploadFileRequest(requestData.getBucket(), requestData.getKey());
        request.setUploadFile(requestData.getFilePath());
        try {
            client.uploadFile(request);
            logger.info("File uploaded: {} to {}/{}", requestData.getFilePath(), requestData.getBucket(), requestData.getKey());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            client.shutdown();
        }
    }

    @Override
    public FileContent getFile(RequestData requestData) throws IOException {
        OSS client = clientFactory.createClient(requestData.getAuthenticationKey(), requestData.getAuthenticationSecret());
        DownloadFileRequest request = new DownloadFileRequest(requestData.getBucket(), requestData.getKey());
        Path tempDownload = Files.createTempFile(Files.createTempDirectory("temp-download"), "process", UUID.randomUUID().toString());
        request.setDownloadFile(tempDownload.toString());
        try {
            DownloadFileResult downloadFileResult = client.downloadFile(request);
            logger.info("File downloaded: {}/{} to {}", requestData.getBucket(), requestData.getKey(), tempDownload);
            ObjectMetadata metadata = downloadFileResult.getObjectMetadata();
            try (InputStream inputStream = Files.newInputStream(tempDownload)) {
                byte[] byteContent = inputStream.readAllBytes();
                FileContent fileContent = FileContent.builder()
                        .content(byteContent)
                        .contentLength((long) byteContent.length)
                        .contentType(metadata.getContentType())
                        .build();
                Files.deleteIfExists(tempDownload);
                Files.deleteIfExists(tempDownload.getParent());
                logger.info("Temp dir deleted: {}", tempDownload);
                return fileContent;
            }

        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            client.shutdown();
        }
    }
}
