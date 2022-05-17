package com.klezovich.rabbitmqtest

import org.springframework.stereotype.Component

@Component
class LoggingReceiver {
    fun receiveMessage(message: String) {
        println("[LOG] Received <$message>")
    }
}