# AWS S3 Connector Runtime

This Springboot application packages the connector into an executable Springboot JAR file with all necessary dependencies
included.

## Running the connector

- Configure your Springboot application for local or camunda platform

```properties
zeebe.client.cloud.region: my-region
zeebe.client.cloud.clusterid: my-cluster-id
zeebe.client.cloud.clientid: my-client-id
zeebe.client.cloud.clientsecret: my-client-secret
```

Or if you use a local runtime:

```properties
zeebe.client.broker.gateway-address=localhost:26500
zeebe.client.security.plaintext=true
```

And add your access and secret key from your AWS as **environment** variables:

```
AWS_ACCESS_KEY=my-access-key
AWS_SECRET_KEY=my-secret-key
```

- Start the Springboot application
- Or build a Docker image with the provided [Dockerfile](../docker/Dockerfile) and use it in a docker-compose environment
with the provided [docker-compose file](../docker/docker-compose.yaml)