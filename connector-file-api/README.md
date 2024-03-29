## Connector File API

This API streamlines file handling for connectors or job workers. It provides interfaces that model file operations
and offers a service to combine local and cloud file handling in one place. 

This way files are stored locally before being uploaded to a cloud provider, where other activities or even processes
can pick them up again. 

Developers are free in the implementation of the interfaces directed to the services. A remote file command can be directed
to any cloud service, a local command can be implemented as in memory stack or even go to a database.

## Overview

<img src="../assets/connector-file-api.png" alt="how it looks like in the modeler" width="1000" />

## Interfaces

### ProcessFileCommand

Called by the process instance to store, load or delete a file. Implemented by the API to combine the local and remote call.

### LocalFileCommand

Implemented by the connector to represent the local storage of the file

### RemoteFileCommand

Implemented by the connector to represent the remote storage of the file

## Authentication Data

In the request data object there are two fields where you can put authentication related content:

- `authenticationKey` representing a user identifier of any kind
- `authenticationSecret` representing a token or password of any kind

The content can be set by you as you need it e.g. a user password combo or AWS_ACCESS_KEY and AWS_SECRET_KEY when
connecting to AWS.