package com.inepex.nyomagestreamprocessor.schemaupdater.updates

import com.google.inject.Inject
import com.inepex.nyomagestreamprocessor.logger.Logger
import com.inepex.nyomagestreamprocessor.schemaupdater.SchemaUpdaterService
import com.inepex.nyomagestreamprocessor.schemaupdater.UpdateBase

class U001 @Inject constructor(private val logger: Logger, updaterService: SchemaUpdaterService) : UpdateBase(1, updaterService){

    override fun update() {
        logger.info("U001 udpated")
    }

    override fun revert() {
        logger.info("U001 reverted")
    }
}
