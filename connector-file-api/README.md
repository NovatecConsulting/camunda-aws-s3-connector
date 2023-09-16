## Connector File API

This API streamlines file handling for connectors or job workers. It provides interfaces that model file operations
and offers a service to combine local and cloud file handling in one place. 

This way files are stored locally before being uploaded to a cloud provider, where other activities or even processes
can pick them up again.

## Interfaces

### ProcessFileCommand

Called by the process instance to store, load or delete a file. Implemented by the API to combine the local and remote call.

### LocalFileCommand

Implemented by the connector to represent the local storage of the file

### RemoteFileCommand

Implemented by the connector to represent the remote storage of the file