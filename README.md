[![Java CI main](https://github.com/NovatecConsulting/camunda-aws-s3-connector/actions/workflows/maven.yml/badge.svg?branch=main)](https://github.com/NovatecConsulting/camunda-aws-s3-connector/actions/workflows/maven.yml)

# AWS S3 Connector

This repository contains a Camunda Outbound Connector to easily interact with files in AWS S3 buckets 🪣:

- ⬆️ Upload a generated file to an AWS S3 bucket
- ⬇️ Download a file from an AWS S3 bucket
- ❎ Delete a file from an AWS S3 bucket
- 📁 Files are saved in the local filesystem to allow interaction between activities

NOTE: This connector will _not_ provide any infrastructur related functionalty like creating or deleting buckets. There are better tools for that...

## Repository Content

* **[AWS S3  Connector implementation](connector-aws-s3-libs/README.md)** to interact with an S3 bucket in AWS

* **[AWS S3 Connector Springboot Runtime](connector-aws-s3-standalone/README.md)** to run as a standalone connector 
without any additional worker logic

* **[Process example](connector-aws-s3-example/README.md)** on how to download/generate content and upload/delete it with the connector

* **[Process File API](connector-file-api/README.md)** to streamline file handling between job workers and the connector

## Connector Compatibility

- JDK 21+
- Camunda Platform v8.4.x
- AWS SDK v2.x
