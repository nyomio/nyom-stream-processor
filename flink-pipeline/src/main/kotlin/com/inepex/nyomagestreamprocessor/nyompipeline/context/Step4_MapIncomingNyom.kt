package com.inepex.nyomagestreamprocessor.nyompipeline.context

import com.inepex.nyomagestreamprocessor.common.dto.Company
import com.inepex.nyomagestreamprocessor.common.dto.Device
import com.inepex.nyomagestreamprocessor.common.dto.User
import com.inepex.nyomagestreamprocessor.pipeline.HasTraceId
import com.inepex.nyomagestreamprocessor.schema.elastic.Mapping
import com.inepex.nyomagestreamprocessor.schema.elastic.nyom.Nyom

data class Step4_MapIncomingNyom(
        override val traceId: String,
        val device: Device,
        val user: User,
        val company: Company?,
        val mapping: Mapping,
        val joinedTraceIds: List<String>,
        val resultNyom: Nyom
) : HasTraceId {
    constructor(
            step3: Step3_QueryObjectMapping,
            joinedTraceIds: List<String>,
            resultNyom: Nyom
    ) : this(step3.traceId, step3.device, step3.user, step3.company, step3.mapping,
            joinedTraceIds, resultNyom)
}
