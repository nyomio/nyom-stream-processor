package com.inepex.nyomagestreamprocessor.schemaupdater

abstract class UpdateBase (protected var version : Int, protected var updaterService: SchemaUpdaterService) {

    init {
        updaterService.registerUpdate(version, this)
    }

    abstract fun update()

    abstract fun revert()
}
