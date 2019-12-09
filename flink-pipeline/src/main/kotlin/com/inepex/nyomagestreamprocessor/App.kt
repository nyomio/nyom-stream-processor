package com.inepex.nyomagestreamprocessor

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Inject
import com.google.inject.Singleton
import com.inepex.nyomagestreamprocessor.cli.CLIService
import com.inepex.nyomagestreamprocessor.config.Configuration
import com.inepex.nyomagestreamprocessor.config.ConfigurationBuilderService
import com.inepex.nyomagestreamprocessor.logger.Logger
import com.inepex.nyomagestreamprocessor.pipeline.Dependencies
import com.inepex.nyomagestreamprocessor.pipeline.PipelinesExecutorService
import com.inepex.nyomagestreamprocessor.schemaupdater.SchemaUpdaterService
import com.inepex.nyomagestreamprocessor.schemaupdater.updates.AllUpdates
import org.apache.commons.cli.*
import org.apache.flink.configuration.TaskManagerOptions
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment

fun main(args: Array<String>) {
    // provide a default value when running from IDE, not in Flink cluster
    if (System.getenv("APP_ID") == null) System.setProperty("APP_ID", "streamprocessorLocal")
    val cli = CLIService()

    try {
        val cmd = cli.parse(args)

        val injector = Guice.createInjector(GuiceModule(if (!cmd.hasOption(
                        "p")) null else cmd.getOptionValue("p")))
        injector.getInstance(App::class.java).execute()
    } catch (error: ParseException) {
        cli.printHelp()
    }

}

@Singleton
class App @Inject constructor(private var schemaUpdaterService: SchemaUpdaterService,
                              private var pipelines: PipelinesExecutorService, private var configuration: Configuration) {

    fun execute() {
        schemaUpdaterService.execute()

        // set the config initialized by Guice for the pipeline
        Dependencies.initWithConfig(configuration)

        pipelines.execute()
    }
}

class GuiceModule constructor(private val runningInIDEWithProfile: String?): AbstractModule() {

    override fun configure() {
        bind(Dependencies::class.java).asEagerSingleton()
        if (runningInIDEWithProfile == null) {
            bind(StreamExecutionEnvironment::class.java).toInstance(StreamExecutionEnvironment.getExecutionEnvironment())
        } else {
            bind(StreamExecutionEnvironment::class.java).toInstance(StreamExecutionEnvironment.createLocalEnvironment(1,
                    org.apache.flink.configuration.Configuration().apply {
                        setInteger(TaskManagerOptions.NUM_TASK_SLOTS, 4)
                    }))
        }
        bind(AllUpdates::class.java).asEagerSingleton()
        bind(Configuration::class.java).toInstance(ConfigurationBuilderService.build(runningInIDEWithProfile))
        bind(Logger::class.java).toInstance(Logger())
    }
}

