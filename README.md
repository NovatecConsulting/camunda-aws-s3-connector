[![Java CI main](https://github.com/NovatecConsulting/camunda-aws-s3-connector/actions/workflows/maven.yml/badge.svg?branch=main)](https://github.com/NovatecConsulting/camunda-aws-s3-connector/actions/workflows/maven.yml)

# AWS S3 Connector

This repository contains a Camunda Outbound Connector to easily interact with files in AWS S3 buckets 🪣:

- ⬆️ Upload a generated file to an AWS S3 bucket
- ⬇️ Download a file from an AWS S3 bucket
- ❎ Delete a file from an AWS S3 bucket
- 📁 Files are saved in the local filesystem to allow interaction between activities

## Content

* **[AWS S3  Connector implementation](connector-aws-s3-libs/README.md)** to interact with an S3 bucket in AWS

* **[AWS S3 Connector Springboot Runtime](connector-aws-s3-standalone/README.md)** to run as a standalone connector 
without any additional worker logic

* **[Process example](connector-aws-s3-example/README.md)** on ho to generate content and upload it with the connector

* **[Process File API](connector-file-api/README.md)** to streamline file handling between job workers and the connector

## Compatibility

- JDK 17+
- Camunda Platform v8.2.x
- Connector SDK v0.23.x
- AWS SDK v2.20.x
