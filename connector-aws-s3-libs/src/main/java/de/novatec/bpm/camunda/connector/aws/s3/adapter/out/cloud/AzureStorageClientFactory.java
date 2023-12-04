package de.novatec.bpm.camunda.connector.aws.s3.adapter.out.cloud;

import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenCredential;
import com.azure.core.credential.TokenRequestContext;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class AzureStorageClientFactory {

    private String azureEndpoint;

    public AzureStorageClientFactory() {
    }

    public AzureStorageClientFactory(String azureEndpoint) {
        this.azureEndpoint = azureEndpoint;
    }

    private static final Logger logger = LoggerFactory.getLogger(AzureStorageClientFactory.class);

    public BlobServiceClient createClient(String sasToken) {
        return new BlobServiceClientBuilder()
                .endpoint(azureEndpoint)
                .sasToken(sasToken)
                .buildClient();
    }

}
