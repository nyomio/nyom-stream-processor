package com.inepex.nyomagestreamprocessor.schema.elastic.nyom

import com.inepex.nyomagestreamprocessor.schema.elastic.Mapping

data class Nyom (val deviceId: Long, val timestampMillis: Long, var location: Location,
                 val event: Event, var status: Status, var mapping: Mapping)
