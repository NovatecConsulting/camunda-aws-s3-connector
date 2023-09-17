[![Java CI main](https://github.com/NovatecConsulting/camunda-aws-s3-connector/actions/workflows/maven.yml/badge.svg?branch=main)](https://github.com/NovatecConsulting/camunda-aws-s3-connector/actions/workflows/maven.yml)

# AWS S3 Connector

Camunda Outbound Connector to interact with the content of an S3 bucket

## Content

* 🔌 **[AWS S3 Connector implementation](connector-aws-s3/README.md)** to interact with an S3 bucket in AWS

* 👉🏻 **[Process example](connector-aws-s3-example/README.md)** on ho to generate content and upload it with the connector

* 📁 **[File API](connector-file-api/README.md)** to streamline file handling between job workers and the connector

## Compatibility

- JDK 17+
- Camunda Platform v8.2.x
- Connector SDK v0.23.x
- AWS SDK v2.20.x

## Connector Features

- Upload a generated file to an AWS S3 bucket
- Delete a file from an AWS S3 bucket
- Download a file from an AWS S3 bucket
- Files are saved in the local filesystem to allow interaction between activities
