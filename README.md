# AWS S3 Connector

Camunda Outbound Connector to interact with the content of an S3 bucket

## AWS Setup
- S3 bucket (non-public) with server-side encryption and versioning enabled
- IAM User with putObject and deleteObject rights to the bucket
- Access key and Secret key for the IAM user

## Operations
- put a file into S3
  - identified by `bucketName/objectKey`
- delete a file from S3
  - identified by `bucketName/objectKey`