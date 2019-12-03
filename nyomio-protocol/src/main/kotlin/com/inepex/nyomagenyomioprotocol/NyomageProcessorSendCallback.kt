package com.inepex.nyomagenyomioprotocol

import org.apache.kafka.clients.producer.Callback
import org.apache.kafka.clients.producer.RecordMetadata
import java.lang.Exception

class NyomageProcessorSendCallback constructor(
        count: Int,
        private val callback: (error: Throwable?) -> Unit
) : Callback{

    private var counter = count

    @Synchronized
    override fun onCompletion(metadata: RecordMetadata?, exception: Exception?) {
        if (exception != null) {
            callback(exception)
        } else {
            counter--
            if (counter == 0) {
                callback(null)
            }
        }
    }
}
