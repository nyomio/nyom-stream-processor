# Interface to query tracked object to device mapping

There can be multiple actual mappings for a device.

Query all mappings by setting timestamp to 0.


* Request
```
{"jsonrpc": "2.0", "method": "getChangedDeviceToObjectMappingsSince", "params": { "timestampMillis": 1559649998000 }}
```

* Response
```
{"jsonrpc": "2.0", 
  "result": { 
    "changed": [
      {"deviceId": "1", "objectId": "1", "begin": 1559649998000, end: null},
      {"deviceId": "2", "objectId": "2", "begin": 1559649998000, end: 1559649998000}
    ]
   }
}
```
