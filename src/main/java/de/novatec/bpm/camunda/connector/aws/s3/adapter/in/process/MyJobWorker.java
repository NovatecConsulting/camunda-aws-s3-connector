package de.novatec.bpm.camunda.connector.aws.s3.adapter.in.process;

import de.novatec.bpm.camunda.connector.aws.s3.usecase.out.LocalFileCommand;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class MyJobWorker {

    private static final Logger logger = LoggerFactory.getLogger(MyJobWorker.class);
    private final LocalFileCommand localFileCommand;

    public MyJobWorker(LocalFileCommand localFileCommand) {
        this.localFileCommand = localFileCommand;
    }

    @JobWorker(type = "generate-file")
    public Map<String, Object> generateFile(ActivatedJob job) throws IOException {
        logger.info("Received job {}", job.getKey());
        byte[] myFileContent = "This is some random file".getBytes(StandardCharsets.UTF_8);
        String generatedFilePath = String.format("results/%s/my-file.txt", job.getProcessInstanceKey());
        logger.info("Result file generated");
        localFileCommand.saveFile(myFileContent, generatedFilePath);
        return Map.of("generatedFilePath", generatedFilePath, "generatedFileContentType", "text/plain");
    }

    @JobWorker(type = "validate-report")
    public Map<String, Object> validateReport(ActivatedJob job, @Variable String filePath) throws IOException {
        logger.info("Received job {}", job.getKey());
        byte[] content = localFileCommand.loadFile(filePath);
        if (content.length > 0) {
            logger.info("Report has {} bytes and is valid", content.length);
            return Map.of("reportValid", true);
        } else {
            logger.info("Report has {} bytes and is invalid", content.length);
            return Map.of("reportValid", false);
        }

    }

}
