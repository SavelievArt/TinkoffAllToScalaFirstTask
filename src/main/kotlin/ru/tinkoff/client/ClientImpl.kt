package ru.tinkoff.client

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Duration
import kotlin.random.Random

class SimpleClient : Client {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(SimpleClient::class.java)
    }

    override fun getApplicationStatus1(id: String): Response {
        logger.info("Вызов сервиса 1")
        fallAsleep()
        return handleAnswer(id, "Сервис 1")
    }

    override fun getApplicationStatus2(id: String): Response {
        logger.info("Вызов сервиса 2")
        fallAsleep()
        return handleAnswer(id, "Сервис 2")
    }

    private fun fallAsleep() = Thread.sleep(Random.nextLong(0, 500))

    private fun generateAnswer() = Random.nextInt(0, 3)

    private fun handleAnswer(id: String, serviceName: String): Response {
        return when (generateAnswer()) {
            0 -> Response.Success("$serviceName - ОК", id)
            1 -> Response.RetryAfter(Duration.ofSeconds(Random.nextLong(1, 5)))
            else -> Response.Failure(Exception("$serviceName - Что-то пошло не так"))
        }
    }
}