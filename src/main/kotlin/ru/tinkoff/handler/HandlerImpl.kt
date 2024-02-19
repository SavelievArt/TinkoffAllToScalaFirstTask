package ru.tinkoff.handler

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import ru.tinkoff.client.Client
import ru.tinkoff.client.Response
import ru.tinkoff.utils.toApplicationStatusResponse
import java.time.Duration


class HandlerImpl(private val client: Client) : Handler {
    override fun performOperation(id: String): ApplicationStatusResponse {
        var retriesCount = 0
        var lastRequestTime: Duration?

        return try {
            runBlocking {
                val startTime = System.currentTimeMillis()
                var timeElapsed: Duration

                var response: Response

                do {
                    val response1Deferred = async { client.getApplicationStatus1(id) }
                    val response2Deferred = async { client.getApplicationStatus2(id) }

                    val response1 = response1Deferred.await()
                    val response2 = response2Deferred.await()

                    val endTime = System.currentTimeMillis()
                    timeElapsed = Duration.ofMillis(endTime - startTime)

                    when {
                        response1 is Response.Success -> return@runBlocking response1.toApplicationStatusResponse()
                        response2 is Response.Success -> return@runBlocking response2.toApplicationStatusResponse()
                        response1 is Response.Failure || response2 is Response.Failure -> {
                            retriesCount++
                            lastRequestTime = timeElapsed
                            return@runBlocking ApplicationStatusResponse.Failure(lastRequestTime, retriesCount)
                        }
                        response1 is Response.RetryAfter -> {
                            response = response1
                            if (response2 is Response.RetryAfter && response2.delay < response1.delay) {
                                response = response2
                            }
                            retriesCount++
                        }
                        else -> {
                            response = response2
                            retriesCount++
                        }
                    }

                    delay((response as Response.RetryAfter).delay.toMillis())
                } while (timeElapsed.toMillis() < 15000)

                lastRequestTime = timeElapsed
                ApplicationStatusResponse.Failure(lastRequestTime, retriesCount)
            }
        } catch (e: TimeoutCancellationException) {
            ApplicationStatusResponse.Failure(null, 0)
        }
    }
}