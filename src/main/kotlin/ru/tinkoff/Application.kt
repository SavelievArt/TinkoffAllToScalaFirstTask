package ru.tinkoff

import ru.tinkoff.client.SimpleClient
import ru.tinkoff.handler.HandlerImpl
import java.util.*

fun main() {
    val client = SimpleClient()
    val handler = HandlerImpl(client)
    println(handler.performOperation(UUID.randomUUID().toString()))
}