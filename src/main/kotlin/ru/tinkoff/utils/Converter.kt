package ru.tinkoff.utils

import ru.tinkoff.client.Response
import ru.tinkoff.handler.ApplicationStatusResponse

fun Response.Success.toApplicationStatusResponse() = ApplicationStatusResponse.Success(this.applicationId, this.applicationStatus)