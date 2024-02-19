package ru.tinkoff.client

interface Client {
    //блокирующий вызов сервиса 1 для получения статуса заявки
    fun getApplicationStatus1(id: String): Response

    //блокирующий вызов сервиса 2 для получения статуса заявки
    fun getApplicationStatus2(id: String): Response
}