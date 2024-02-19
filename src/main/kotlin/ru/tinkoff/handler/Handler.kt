package ru.tinkoff.handler

fun interface Handler {
    fun performOperation(id: String): ApplicationStatusResponse
}