package com.inepex.nyomagestreamprocessor.cli

import org.apache.commons.cli.*

class CLIService {

    val options = Options().apply {
        addOption("h", "help", false, "print this message")
        addOption("p", "runningInIDEWithProfile", true, "execute in IDE. " +
                "arg is the name of the config profile to use. Without this the configuration " +
                "properties are read from environment variables.")
    }

    var commandLine: CommandLine? = null

    fun parse(args: Array<String>): CommandLine {
        commandLine = DefaultParser().parse(options, args)
        if (commandLine!!.hasOption("h")) throw ParseException("")
        return commandLine!!
    }

    fun printHelp() {
        HelpFormatter().printHelp("com.inepex.nyomagestreamprocessor.AppKt",
                "", options, "", true)

    }
}
