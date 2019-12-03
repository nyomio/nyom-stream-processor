package com.inepex.nyomagestreamprocessor.nyompipeline.mapincomingnyom

import com.inepex.nyomagestreamprocessor.api.incomingnyom.IncomingNyomEntryOuterClass
import com.inepex.nyomagestreamprocessor.api.incomingnyom.getTimestamp
import com.inepex.nyomagestreamprocessor.schema.elastic.nyom.Nyom
import com.inepex.nyomagestreamprocessor.pipeline.Dependencies
import com.inepex.nyomagestreamprocessor.pipeline.PipelineLifecycleHooks
import com.inepex.nyomagestreamprocessor.nyompipeline.context.Step3_QueryObjectMapping
import com.inepex.nyomagestreamprocessor.nyompipeline.context.Step4_MapIncomingNyom
import org.apache.flink.api.common.state.ListState
import org.apache.flink.api.common.state.ListStateDescriptor
import org.apache.flink.api.common.state.ValueState
import org.apache.flink.api.common.state.ValueStateDescriptor
import org.apache.flink.api.common.typeinfo.TypeHint
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

class MapIncomingNyomWindowFunction : ProcessWindowFunction<Step3_QueryObjectMapping, Step4_MapIncomingNyom, String, TimeWindow>() {

    @Transient
    private lateinit var latestNyom: ValueState<Nyom>

    @Transient
    private lateinit var latestNyomWithKnownLocation: ValueState<Nyom>

    @Transient
    private lateinit var shouldBeInterpolatedOnNextLocation: ListState<Nyom>

    override fun open(parameters: Configuration) {
        latestNyom = runtimeContext.getState(ValueStateDescriptor<Nyom>("latestNyom",
                TypeInformation.of(object : TypeHint<Nyom>() {})))
        latestNyomWithKnownLocation = runtimeContext.getState(ValueStateDescriptor<Nyom>("latestNyomWithKnownLocation",
                TypeInformation.of(object : TypeHint<Nyom>() {})))
        shouldBeInterpolatedOnNextLocation = runtimeContext.getListState(ListStateDescriptor<Nyom>(
                "shouldBeInterpolatedOnNextLocation", TypeInformation.of(object : TypeHint<Nyom>() {})))
        PipelineLifecycleHooks.jobIsRunning?.invoke()
    }

    override fun process(key: String, context: Context, elements: MutableIterable<Step3_QueryObjectMapping>, out: Collector<Step4_MapIncomingNyom>) {
        val firstEntry = elements.elementAt(0).incomingNyomEntry
        val firstTimestamp = latestNyom.value()?.timestampMillis ?: firstEntry.getTimestamp()
        val groupsByTimestampDistance = elements.groupBy {
            Math.floor((it.incomingNyomEntry.getTimestamp() - firstTimestamp) / Constants.ENTRY_GROUPING_THRESHOLD_MS.toDouble()).toInt()
        }

        groupsByTimestampDistance.forEach {
            val firstProcessing = it.value.first()
            var timestamp: Long = it.value.first().incomingNyomEntry.getTimestamp() // also Nyom id
            val shouldBeGroupedWithLatestNyom = shouldBeGroupedWithLatestNyom(it.value.first().incomingNyomEntry, firstTimestamp)
            if (shouldBeGroupedWithLatestNyom) {
                /**
                 * reusing the timestamp will result an Elastic update instead of an insert
                 */
                timestamp = latestNyom.value().timestampMillis
            }
            Dependencies.get().mapIncomingNyomService.execute(timestamp, firstProcessing.device.id,
                    firstProcessing.mapping, latestNyom.value(), it.value.map { it.incomingNyomEntry },
                    shouldBeGroupedWithLatestNyom).apply {
                logSkipped(this)
                latestNyom.update(this.nyom)
                if (this.nyom.location.interpolated && latestNyomWithKnownLocation.value() != null) {
                    shouldBeInterpolatedOnNextLocation.add(this.nyom)
                } else {
                    shouldBeInterpolatedOnNextLocation.get().forEach {
                        Dependencies.get().interpolateLocationService.execute(
                                it,
                                latestNyomWithKnownLocation.value(),
                                this.nyom
                        ).apply {
                            out.collect(Step4_MapIncomingNyom(elements.elementAt(0), elements.map { it.traceId },
                                    this))
                        }
                    }
                    shouldBeInterpolatedOnNextLocation.clear()
                }
                if (!this.nyom.location.interpolated) {
                    latestNyomWithKnownLocation.update(this.nyom)
                }
                out.collect(Step4_MapIncomingNyom(elements.elementAt(0), elements.map { it.traceId },
                        this.nyom))
            }
        }
    }

    private fun shouldBeGroupedWithLatestNyom(incomingNyomEntry: IncomingNyomEntryOuterClass.IncomingNyomEntry, firstTimestamp: Long) =
            latestNyom.value() != null && incomingNyomEntry.getTimestamp() - firstTimestamp < Constants.ENTRY_GROUPING_THRESHOLD_MS

    private fun logSkipped(mapResult: MapIncomingNyomService.Result) {
        if (mapResult.numberOfSkippedLocations > 0 || mapResult.numberOfSkippedEvents > 0 ||
                mapResult.numberOfSkippedStatuses > 0) {
            Dependencies.get().logger.info("Skipped some IncomingNyomEntries due to grouping: ${mapResult.numberOfSkippedLocations} locations, " +
                    "${mapResult.numberOfSkippedEvents} events, ${mapResult.numberOfSkippedStatuses} statuses.")
        }
    }

}
