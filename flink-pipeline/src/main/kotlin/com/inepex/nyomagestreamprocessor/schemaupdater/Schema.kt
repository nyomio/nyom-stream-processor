package com.inepex.nyomagestreamprocessor.schemaupdater


object SchemaVersion {
    const val index = "schema_version"
    const val current = "current"

}

object NyomIndexTemplate {
    const val mapping = """
{
    "properties": {
        "timestampMillis": {
            "type": "date",
            "format": "epoch_millis"
        },
        "location": {
            "properties" : {
                "coordinates" : {
                    "type" : "geo_point"
                }
            }
        }
    }
}
        """
}

object TripIndexTemplate {
    const val mapping = """
{
    "properties": {
        "startTimestamp": {
            "type": "date",
            "format": "epoch_millis"
        },
        "stopTimestamp": {
            "type": "date",
            "format": "epoch_millis"
        },
        "startPoint": {
            "type" : "geo_point"
        },
        "stopPoint": {
            "type" : "geo_point"
        },
        "route": {
            "type" : "geo_shape"
        }
    }
}
        """
}
