package ru.tinkoff.client

import java.time.Duration

sealed interface Response {
    data class Success(val applicationStatus: String, val applicationId: String) : Response
    data class RetryAfter(val delay: Duration) : Response
    data class Failure(val ex: Throwable) : Response
}