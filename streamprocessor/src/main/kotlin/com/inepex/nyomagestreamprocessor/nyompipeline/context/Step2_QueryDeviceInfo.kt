package com.inepex.nyomagestreamprocessor.nyompipeline.context

import com.inepex.nyomagestreamprocessor.api.incomingnyom.IncomingNyomEntryOuterClass
import com.inepex.nyomagestreamprocessor.common.dto.Company
import com.inepex.nyomagestreamprocessor.common.dto.Device
import com.inepex.nyomagestreamprocessor.common.dto.User
import com.inepex.nyomagestreamprocessor.pipeline.HasTraceId

data class Step2_QueryDeviceInfo (
        override val traceId: String,
        val incomingNyomEntry: IncomingNyomEntryOuterClass.IncomingNyomEntry,
        val device: Device,
        val user: User,
        val company: Company?
) : HasTraceId
