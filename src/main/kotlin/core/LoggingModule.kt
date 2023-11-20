package core

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import org.koin.dsl.module

val loggingModule = module {
    single<KLogger> { KotlinLogging.logger {} }
    single<Logger> { LoggerImpl(get()) }
}
