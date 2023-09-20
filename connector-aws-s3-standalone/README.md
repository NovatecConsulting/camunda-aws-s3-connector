# AWS S3 Connector Runtime

This Springboot application packages the connector into an executable Springboot JAR file with all necessary dependencies
included.

## Running the connector

- Configure your Springboot application for local or camunda platform

```yaml
zeebe.client.cloud.region: my-region
zeebe.client.cloud.clusterid: my-cluster-id
zeebe.client.cloud.clientid: my-client-id
zeebe.client.cloud.clientsecret: my-client-secret
```

- Or if you use a local runtime:

```yaml
zeebe.client.broker.gateway-address: localhost:26500
zeebe.client.security.plaintext: true
```

NOTE: you don't need an Operate client for an outbound connector therefore I removed the AutoConfiguration for inbound
connectors and the endpoint to Operate from the properties

- Add your access and secret key from your AWS as **environment** variables:

```
AWS_ACCESS_KEY=my-access-key
AWS_SECRET_KEY=my-secret-key
```

- Start the Springboot application
- Or build a Docker image with the provided [Dockerfile](../docker/Dockerfile) and use it in a docker-compose environment
with the provided [docker-compose file](../docker/docker-compose.yaml)

NOTE: the docker image is based on the `arm64v8` architecture since it is developed on an Mx chip by Apple, you can switch this out 
for any matching architecture:

```
FROM arm64v8/openjdk:17
```