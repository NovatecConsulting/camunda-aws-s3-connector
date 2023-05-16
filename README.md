# AWS S3 Connector

Camunda Outbound Connector to interact with the content of an S3 bucket

## Setup

### Connector configuration

| name          | description                                | example                     |
|---------------|--------------------------------------------|-----------------------------|
| accessKey     | the AWS access key for the authorized user | `secrets.AWS_ACCESS_KEY`    |
| secretKey     | the AWS secret key for the authorized user | `secrets.AWS_SECRET_KEY`    |
| region        | the AWS region of your S3 bucket           | eu-central-1                |
| bucketName    | the name of the S3 bucket                  | camunda-s3-connector-bucket |
| objectKey     | path + file name in the s3 bucket          | `="invoice/"+fileName`      |
| operationType | what t do on s3                            | `PUT_OBJECT`                |
| filePath      | absolute path to the file to upload        | `=filePath`                 |
| contentType   | the content type of the content            | `=contentType`              |

NOTE: please do not put secrets directly into your configuration. See the secrets section for more details.

#### How it looks in the modeller
<img src="assets/connector-config-example.png" alt="how it looks like in the modeller" width="400" />

### AWS Resources
- S3 bucket (non-public) with server-side encryption and versioning enabled
- IAM User with putObject and deleteObject rights to the bucket
- Access key and Secret key for the IAM user

#### IAM policy

```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "AllowPutAndDeleteInS3",
            "Effect": "Allow",
            "Action": [
                "s3:PutObject",
                "s3:DeleteObject"
            ],
            "Resource": "arn:aws:s3:::<bucket-name>/*"
        }
    ]
}
```

## Runtime

### Handling secrets
Since your connector needs to run in a custom connector runtime, you cannot just add secrets in the cloud console since
they will not be auto-magically transported into your connector runtime. You can provide them by:

- adding them as environment variables (e.g. when you use the SpringBoot connector runtime)
- adding them as an env file to your docker container

NOTE: This behaviour will most likely be improved in the future

### How to generate content?
The upload is done by resolving a local path to a `File`. Since a process variable is currently limited in size to approx. 
2-4MB (there are some Zeebe chaos engineering tests) the file content should stay in the connector runtime. You can e.g. 
use a `JobWorker` to generate a file and store it in a `/tmp` folder. You can then get the path and set it into a `filePath` 
variable, which you then can reference with a FEEL expression in the `file path` configuration of the connector:

```java
// generate file
File file = pdfService.generate(orderData);

// set as variables to be picked up by connector
variableHandler.setVariable("fileName", String.format("invoice-%s.pdf", orderData.getInvoiceId));
variableHandler.setVariable("filePath", file.getAbsolutePath());
variableHandler.setVariable("contentType", "application/pdf");
```

## Example process
![process.png](assets/process.png)
