package core

interface Logger {
    fun d(message: String)
    fun i(message: String)
    fun e(message: String)
    fun e(error: Throwable)
}
