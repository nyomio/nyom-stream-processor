package com.inepex.nyomagestreamprocessor.nyompipeline.context

import com.inepex.nyomagestreamprocessor.api.incomingnyom.IncomingNyomEntryOuterClass
import com.inepex.nyomagestreamprocessor.schema.elastic.Mapping
import com.inepex.nyomagestreamprocessor.pipeline.HasTraceId
import nyomio.app.core.devicemanager.getdeviceinfo.Device
import nyomio.app.core.devicemanager.getdeviceinfo.Organization

data class Step3_QueryObjectMapping(
        override val traceId: String,
        val incomingNyomEntry: IncomingNyomEntryOuterClass.IncomingNyomEntry,
        val device: Device,
        val organization: Organization?,
        val mapping: Mapping
) : HasTraceId {
    constructor(
            step2: Step2_QueryDeviceInfo,
            mapping: Mapping
    ) : this(step2.traceId, step2.incomingNyomEntry, step2.device,
            step2.organization, mapping)
}
