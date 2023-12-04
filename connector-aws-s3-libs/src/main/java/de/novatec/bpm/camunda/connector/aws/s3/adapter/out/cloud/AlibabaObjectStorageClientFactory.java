package de.novatec.bpm.camunda.connector.aws.s3.adapter.out.cloud;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

import java.net.URI;

public class AlibabaObjectStorageClientFactory {

    private String endpoint;

    public AlibabaObjectStorageClientFactory() {
    }

    public AlibabaObjectStorageClientFactory(String endpoint) {
        this.endpoint = endpoint;
    }

    private static final Logger logger = LoggerFactory.getLogger(AlibabaObjectStorageClientFactory.class);

    public OSS createClient(String accessKeyId, String accessKeySecret) {
        logger.info("Client initialized: {}", endpoint);
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

}
