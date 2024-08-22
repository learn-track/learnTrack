package ch.learntrack.core

import ch.learntrack.core.plugins.configureFrameworks
import ch.learntrack.core.plugins.configureRouting
import ch.learntrack.core.plugins.configureSerialization
import io.ktor.server.application.Application

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureFrameworks()
    configureRouting()
}
