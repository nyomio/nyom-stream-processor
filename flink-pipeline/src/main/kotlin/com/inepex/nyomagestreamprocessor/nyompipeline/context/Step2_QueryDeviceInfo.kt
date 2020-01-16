package com.inepex.nyomagestreamprocessor.nyompipeline.context

import com.inepex.nyomagestreamprocessor.api.incomingnyom.IncomingNyomEntryOuterClass
import com.inepex.nyomagestreamprocessor.pipeline.HasTraceId
import nyomio.app.core.devicemanager.getdeviceinfo.Device
import nyomio.app.core.devicemanager.getdeviceinfo.Organization

data class Step2_QueryDeviceInfo (
        override val traceId: String,
        val incomingNyomEntry: IncomingNyomEntryOuterClass.IncomingNyomEntry,
        val device: Device,
        val organization: Organization?
) : HasTraceId
