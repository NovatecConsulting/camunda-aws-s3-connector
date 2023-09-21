## AWS S3 Connector Example

### Use case

Your review process starts when a new file is uploaded to an S3 bucket. The file is downloaded, validated and the result 
published to S3 again

## Example process
![process.png](../assets/example-process.png)

## Setup

In this example the use of connectors and job workers is mixed together. The connector runtime is added as a dependency
while the job workers logic is located here. Both use the file api to access local and remote files.

## Running the example
- Configure your Springboot application for local or camunda platform

```yaml
zeebe.client.cloud.region: my-region
zeebe.client.cloud.clusterid: my-cluster-id
zeebe.client.cloud.clientid: my-client-id
zeebe.client.cloud.clientsecret: my-client-secret
```

Or if you use a local runtime:

```yaml
zeebe.client.broker.gateway-address: localhost:26500
zeebe.client.security.plaintext: true
```

NOTE: you don't need an Operate client for an outbound connector therefore I removed the AutoConfiguration for inbound 
connectors and the endpoint to Operate from the properties

- Add your access and secret key from your AWS as environment variables:

```
AWS_ACCESS_KEY=my-access-key
AWS_SECRET_KEY=my-secret-key
```

- Start the Springboot application
- Or build a Docker image with the provided [Dockerfile](../docker/Dockerfile) and use it in a docker-compose environment
  with the provided [docker-compose file](../docker/docker-compose.yaml)
  - You need to change the executed jar file in the Dockerfile from `*-standalone` to `*-example` to do so

NOTE: the docker image is based on the `arm64v8` architecture since it is developed on an Mx chip by Apple, you can switch this out
for any matching architecture:

```
FROM arm64v8/openjdk:17
```
 
- Start a process instance with the following variable

```json
{
  "report": {
    "region": "eu-central-1",
    "bucket": "my-connector-bucket",
    "key":"reports/report-xyz.txt"
  }
}
```

NOTE: the bucket and the report file must exist on your AWS Account. The AWS setup is described in 
the [connector's README](../connector-aws-s3-libs/README.md)

## Integration testing the process with AWS

To do an integration test I use the following technologies:

- Testcontainer [localstack module](https://java.testcontainers.org/modules/localstack/) to mimik AWS S3
- SpringBootTest to run my process application
- SpringZeebeTest to run an in-memory Zeebe process engine and assert the state of the process

## Further improvement ideas

- Use an inbound connector to get notified via SQS or SNS if there are new files instead of setting it explicitly via
variables
