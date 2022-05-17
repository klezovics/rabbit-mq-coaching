package com.klezovich.rabbitmqtest

import org.springframework.stereotype.Component


class AkReceiver {
    fun receiveMessage(message: String) {
        println("Received <$message>")
    }
}