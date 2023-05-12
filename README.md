# AWS S3 Connector

Camunda Outbound Connector to interact with the content of an S3 bucket

## Setup

### Connector configuration

|name         |description                                                      |example                    |
|-------------|-----------------------------------------------------------------|---------------------------|
|accessKey    |the AWS access key for the authorized user                       |`secrets.AWS_ACCESS_KEY`   |
|secretKey    |the AWS secret key for the authorized user                       |`secrets.AWS_SECRET_KEY`   |
|region       |the AWS region of your S3 bucket                                 |eu-central-1               |
|bucketName   |the name of the S3 bucket                                        |camunda-s3-connector-bucket|
|objectKey    |path + file name in the s3 bucket                                |`="invoice/"+fileName`     |
|operationType|upload or delete                                                 |                           |
|content      |base64 encoded string or feel expression representing the content|`=fileContent`             |
|contentType  |the content type of the content                                  |`=fileContentType`         |

NOTE: please do not put secrets directly into your configuration. Please use the [secret provider mechanism](https://docs.camunda.io/docs/components/connectors/use-connectors/#using-secrets) provided by camunda 8

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

### How to generate content?
The upload is done by converting the content from base64 string to a byte array input stream. You can e.g. use a `JobWorker` to generate a pdf and
convert the content to a base64 encoded string and save it to a process variable `pdfContent`. Now you can reference it with a FEEL expression in the `content` 
configuration of the connector.

```java
// read pdf as bytes
byte[] invoiceBytes = Files.readAllBytes(Paths.get("tmp/invoice.pdf)); 

// encode in base64
byte[] encoded = java.util.Base64.getEncoder().encode(invoiceBytes);
String base64String = new String(encoded);

// set as variables to be picked up by connector
variableHandler.setVariable("fileContent", base64String)
variableHandler.setVariable("fileContentType", "application/pdf")
variableHandler.setVariable("fileName", "invoice.pdf")
```

## Example process
![process.png](assets/process.png)
