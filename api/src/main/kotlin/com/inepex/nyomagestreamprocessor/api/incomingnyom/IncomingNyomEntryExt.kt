package com.inepex.nyomagestreamprocessor.api.incomingnyom

fun IncomingNyomEntryOuterClass.IncomingNyomEntry.getTimestamp(): Long {
    return when (this.entryCase) {
        IncomingNyomEntryOuterClass.IncomingNyomEntry.EntryCase.EVENTS -> this.events.timestamp
        IncomingNyomEntryOuterClass.IncomingNyomEntry.EntryCase.STATUS -> this.status.timestamp
        IncomingNyomEntryOuterClass.IncomingNyomEntry.EntryCase.LOCATION -> this.location.timestamp
        else -> throw RuntimeException("Invalid case: ${this.entryCase}")
    }
}

