package com.inepex.nyomagestreamprocessor.nyompipeline.context

import com.inepex.nyomagestreamprocessor.api.incomingnyom.IncomingNyomEntryOuterClass
import com.inepex.nyomagestreamprocessor.common.dto.Company
import com.inepex.nyomagestreamprocessor.common.dto.Device
import com.inepex.nyomagestreamprocessor.schema.elastic.Mapping
import com.inepex.nyomagestreamprocessor.common.dto.User
import com.inepex.nyomagestreamprocessor.pipeline.HasTraceId

data class Step3_QueryObjectMapping(
        override val traceId: String,
        val incomingNyomEntry: IncomingNyomEntryOuterClass.IncomingNyomEntry,
        val device: Device,
        val user: User,
        val company: Company?,
        val mapping: Mapping
) : HasTraceId {
    constructor(
            step2: Step2_QueryDeviceInfo,
            mapping: Mapping
    ) : this(step2.traceId, step2.incomingNyomEntry, step2.device, step2.user,
            step2.company, mapping)
}
