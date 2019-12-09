package com.inepex.nyomagestreamprocessor.testapp.common.kafkaproducer

import com.google.inject.Inject
import com.google.inject.Singleton
import com.inepex.nyomagestreamprocessor.api.incomingnyom.IncomingNyomEntryOuterClass
import com.inepex.nyomagestreamprocessor.nyompipeline.Schema
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord

@Singleton
class Client
    @Inject constructor(
            kafkaProducerProperties: KafkaProducerProperties
    ){

    private val kafkaConnection: KafkaProducer<String, ByteArray> = KafkaProducer(kafkaProducerProperties.get())

    fun send(entry: IncomingNyomEntryOuterClass.IncomingNyomEntry) {
        send(listOf(entry))
    }

    fun send(entryList: List<IncomingNyomEntryOuterClass.IncomingNyomEntry>) {
        if (entryList.isEmpty()) return
        Thread(Runnable {
            entryList.forEach {
                kafkaConnection.send(ProducerRecord(Schema.INCOMING_NYOM_TOPIC, it.toByteArray())).get()
            }
        }).start()
    }
}
