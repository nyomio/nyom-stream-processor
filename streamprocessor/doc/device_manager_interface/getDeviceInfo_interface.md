# Interface to query device info, owner info, company info by device id
* Request
```
{"jsonrpc": "2.0", "method": "getDeviceInfo", "params": {
  "deviceId" : 100000
}}
```

* Response
```
{"jsonrpc": "2.0", 
  "result": {
    "device": {
      "name" : "Device1"
      "rules" : TODO 
    },
    "user": {
      "id" : 1
      "name" : "User1"
      "rules" : TODO 
      
    },
    "companyOrNull": {
      "id" : 1
      "name" : "Company1"
      "rules" : TODO 
      "tracked_object_mapping_url": "http://company.test/mapping"
      "url2"
      "url3"
    }
  }

}
```
