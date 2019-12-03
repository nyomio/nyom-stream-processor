# nyomage-nyomio_protocol

## generate cert and convert key to pkcs8 for grpc
```
openssl genrsa -out dev.key 2048
openssl req -new -key dev.key -out dev.csr
openssl x509 -req -days 365 -in dev.csr -signkey dev.key -out dev.crt -extfile certSubjectAltName.ext
openssl pkcs8 -topk8 -nocrypt -in dev.key -out dev.pkcs8 
```

