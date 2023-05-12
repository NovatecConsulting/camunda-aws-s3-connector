# AWS S3 Connector
Camunda Outbound Connector to interact with the content of an S3 bucket

## Configuration

|name         |description                                   |example                    |
|-------------|----------------------------------------------|---------------------------|
|accessKey    |the AWS access key for the authorized user    |<nice try>                 |
|secretKey    |the AWS secret key for the authorized user    |<nice try>                 |
|region       |the AWS region of your S3 bucket              |eu-central-1               |
|bucketName   |the name of the S3 bucket                     |camunda-s3-connector-bucket|
|objectKey    |path + file name in the s3 bucket             |invoice/my-invoice.pdf     |
|operationType|upload or delete                              |                           |
|content      |base64 encoded string representing the content|bm92YXRlYw==               |
|contentType  |the content type of the content               |application/pdf            |

## AWS Setup
- S3 bucket (non-public) with server-side encryption and versioning enabled
- IAM User with putObject and deleteObject rights to the bucket
- Access key and Secret key for the IAM user

### IAM policy

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

## Example process
![process.png](assets/process.png)
