package core

import io.github.oshai.kotlinlogging.KLogger

class LoggerImpl(private val logger: KLogger) : Logger {
    override fun d(message: String) {
        logger.debug { message }
    }

    override fun i(message: String) {
        logger.info { message }
    }

    override fun e(message: String) {
        logger.error { message }
    }

    override fun e(error: Throwable) {
        logger.error { error }
    }
}
