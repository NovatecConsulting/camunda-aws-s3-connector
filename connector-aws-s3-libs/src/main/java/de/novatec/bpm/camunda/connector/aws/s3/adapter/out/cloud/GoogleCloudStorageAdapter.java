package de.novatec.bpm.camunda.connector.aws.s3.adapter.out.cloud;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
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

public class GoogleCloudStorageAdapter implements RemoteFileCommand {

    private static final Logger logger = LoggerFactory.getLogger(GoogleCloudStorageAdapter.class);

    private final GoogleCloudStorageClientFactory clientFactory;

    public GoogleCloudStorageAdapter(GoogleCloudStorageClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    public void deleteFile(RequestData requestData) throws IOException {
        try (Storage client = clientFactory.createClient(requestData.getAuthenticationKey())) {
            BlobId id = BlobId.of(requestData.getBucket(), requestData.getKey());
            client.delete(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void putFile(RequestData requestData, FileContent fileContent) {
        try (Storage client = clientFactory.createClient(requestData.getAuthenticationKey())) {
            BlobId id = BlobId.of(requestData.getBucket(), requestData.getKey());
            BlobInfo blobInfo = BlobInfo.newBuilder(id)
                    .setContentType(fileContent.getContentType())
                    .build();
            client.createFrom(blobInfo, new ByteArrayInputStream(fileContent.getContent()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FileContent getFile(RequestData requestData) {
        try (Storage client = clientFactory.createClient(requestData.getAuthenticationKey())) {
            BlobId id = BlobId.of(requestData.getBucket(), requestData.getKey());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            client.downloadTo(id, outputStream);
            byte[] content = outputStream.toByteArray();
            return FileContent.builder()
                    .content(content)
                    .contentLength((long) content.length)
                    .contentType(requestData.getContentType())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
