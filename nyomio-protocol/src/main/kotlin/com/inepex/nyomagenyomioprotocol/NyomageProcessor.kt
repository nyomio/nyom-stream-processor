package com.inepex.nyomagenyomioprotocol

import com.google.inject.Inject
import com.google.inject.Singleton
import com.inepex.nyomagenyomioprotocol.logger.Logger
import com.inepex.nyomagenyomioprotocol.statuslogger.StatusLogger
import com.inepex.nyomagestreamprocessor.api.incomingnyom.IncomingNyomEntryOuterClass
import com.inepex.nyomagestreamprocessor.api.incomingnyom.Nyomage
import com.inepex.nyomagestreamprocessor.api.incomingnyom.getTimestamp
import io.reactivex.*
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord

const val E5 = 100000.0
const val deciMetersPerSecondsToKnots = 0.194384449412

data class ProcessingContext(val nativeId: String, val nyomage: Nyomage, val callbackEmitter: SingleEmitter<Boolean>)

@Singleton
class NyomageProcessor
@Inject constructor(
        private val statusLogger: StatusLogger,
        private val logger: Logger, serverProperties: KafkaServerProperties) {

    private val kafkaConnection: KafkaProducer<String, ByteArray> = KafkaProducer(serverProperties.get())

    private var nyomageFlowEmitter: ObservableEmitter<ProcessingContext>? = null

    private val nyomageFlow = Observable.create<ProcessingContext> {
        nyomageFlowEmitter = it
    }

    init {
        processFlow()
    }

    fun processNyomage(nativeId:String, nyomage: Nyomage) = Single.create<Boolean> {
        nyomageFlowEmitter?.onNext(ProcessingContext(nativeId, nyomage, it))
    }

    private fun processFlow() {
        nyomageFlow.flatMapSingle { ctx ->
            send(ctx.nativeId, ctx.nyomage)
                    .map {
                        ctx.callbackEmitter.onSuccess(true)
                    }
        }.subscribe({
//            logger.error("Flow has been completed, but it wasn't intended.")
        }, {
            logger.error("Error occured in flow.", it)
        })
    }

    private fun send(nativeId: String, nyomage: Nyomage) = Single.create<Boolean> { emitter ->
        val nyoms = map(nativeId, nyomage)
        if (nyoms.isEmpty()) {
            emitter.onSuccess(true)
        } else {
            statusLogger.incReceivedNyom(nyoms.size.toLong())
            val callback = NyomageProcessorSendCallback(nyoms.size) {
                if (it != null) {
                    emitter.onError(it)
                } else {
                    statusLogger.incrProcessedNyom(nyoms.size.toLong())
                    emitter.onSuccess(true)
                }
            }
            for (nyom in nyoms) {
                kafkaConnection.send(ProducerRecord(INCOMING_NYOM_TOPIC, "partition",
                        nyom.toByteArray()),
                        callback)
            }
        }
    }

    private fun map(nativeId: String, nyomage: Nyomage): List<IncomingNyomEntryOuterClass.IncomingNyomEntry> {
        val entryList = mutableListOf<IncomingNyomEntryOuterClass.IncomingNyomEntry>()
        nyomage.locationList.map {
            val entry = IncomingNyomEntryOuterClass.IncomingNyomEntry.newBuilder()
            entry.nativeId = nativeId
            entry.location = it
            entryList.add(entry.build())
        }

        nyomage.eventsList.map {
            val entry = IncomingNyomEntryOuterClass.IncomingNyomEntry.newBuilder()
            entry.nativeId = nativeId
            entry.events = it
            entryList.add(entry.build())
        }

        nyomage.statusList.map {
            val entry = IncomingNyomEntryOuterClass.IncomingNyomEntry.newBuilder()
            entry.nativeId = nativeId
            entry.status = it
            entryList.add(entry.build())
        }

        entryList.sortBy {
            it.getTimestamp()
        }

        return entryList
    }

}
