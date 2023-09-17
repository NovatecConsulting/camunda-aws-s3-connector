## AWS S3 Connector Example

### Use case

Your review process starts when a new file is uploaded to an S3 bucket. The file is downloaded, validated and the result 
published to S3 again

## Example process
![process.png](../assets/example-process.png)

## Setup

In this example the use of connectors and job workers is mixed together. The connector runtime is added as a dependency
while the job workers logic is located here. Both use the file api to access local and remote files.

## Further improvement ideas

- Use an inbound connector to get notified via SQS or SNS if there are new files