package de.novatec.bpm.camunda.connector.aws.s3.adapter.out.cloud;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import de.novatec.bpm.camunda.connector.file.api.RemoteFileCommand;
import de.novatec.bpm.camunda.connector.file.api.impl.model.FileContent;
import de.novatec.bpm.camunda.connector.file.api.impl.model.RequestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AzureStorageAdapter implements RemoteFileCommand {

    private static final Logger logger = LoggerFactory.getLogger(AzureStorageAdapter.class);

    private final AzureStorageClientFactory clientFactory;

    public AzureStorageAdapter(AzureStorageClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    public void deleteFile(RequestData requestData) {
        BlobServiceClient client = clientFactory.createClient(requestData.getAuthenticationSecret());
        BlobContainerClient containerClient = client.createBlobContainer(requestData.getBucket());
        BlobClient blobClient = containerClient.getBlobClient(requestData.getKey());
        blobClient.deleteIfExists();
    }

    @Override
    public void putFile(RequestData requestData, FileContent fileContent) {
        BlobServiceClient client = clientFactory.createClient(requestData.getAuthenticationSecret());
        BlobContainerClient containerClient = client.createBlobContainer(requestData.getBucket());
        BlobClient blobClient = containerClient.getBlobClient(requestData.getKey());
        ByteArrayInputStream content = new ByteArrayInputStream(fileContent.getContent());
        blobClient.upload(content, fileContent.getContentLength());
    }

    @Override
    public FileContent getFile(RequestData requestData) {
        BlobServiceClient client = clientFactory.createClient(requestData.getAuthenticationSecret());
        BlobContainerClient containerClient = client.createBlobContainer(requestData.getBucket());
        BlobClient blobClient = containerClient.getBlobClient(requestData.getKey());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        blobClient.downloadStream(outputStream);
        byte[] content = outputStream.toByteArray();
        return FileContent.builder()
                .content(content)
                .contentLength((long)content.length)
                .contentType(requestData.getContentType())
                .build();
    }
}
