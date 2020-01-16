package com.inepex.nyomagestreamprocessor.nyompipeline.context

import com.inepex.nyomagestreamprocessor.pipeline.HasTraceId
import com.inepex.nyomagestreamprocessor.schema.elastic.Mapping
import com.inepex.nyomagestreamprocessor.schema.elastic.nyom.Nyom
import nyomio.app.core.devicemanager.getdeviceinfo.Device
import nyomio.app.core.devicemanager.getdeviceinfo.Organization

data class Step4_MapIncomingNyom(
        override val traceId: String,
        val device: Device,
        val organization: Organization?,
        val mapping: Mapping,
        val joinedTraceIds: List<String>,
        val resultNyom: Nyom
) : HasTraceId {
    constructor(
            step3: Step3_QueryObjectMapping,
            joinedTraceIds: List<String>,
            resultNyom: Nyom
    ) : this(step3.traceId, step3.device, step3.organization, step3.mapping,
            joinedTraceIds, resultNyom)
}
