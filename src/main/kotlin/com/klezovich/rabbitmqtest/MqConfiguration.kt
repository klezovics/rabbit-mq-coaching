package com.klezovich.rabbitmqtest

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.lang.Exception
import java.util.*

@Configuration
class MqConfiguration {

    companion object {
        val akExchange = "ak-exchange"

        val queueAkBtc = "ak-btc"
        val queueAkEth = "ak-eth"
    }

    // E-B-Q linkage
    @Bean
    fun qBtc(): Queue {
        return Queue(queueAkBtc, false)
    }

    @Bean
    fun qEth(): Queue {
        return Queue(queueAkEth, false)
    }

    @Bean
    fun exchange(): DirectExchange {
        return DirectExchange(akExchange)
    }

    @Bean
    fun bBtc(@Qualifier("qBtc")queue: Queue, exchange: DirectExchange): Binding {
        return BindingBuilder.bind(queue).to(exchange).with("ak.btc")
    }

    @Bean
    fun bEth(@Qualifier("qEth")queue: Queue, exchange: DirectExchange): Binding {
        return BindingBuilder.bind(queue).to(exchange).with("ak.eth")
    }

    // Consumer configuration
    class LoggingReceiver {
        fun receiveMessage(message: String) {
            println("[LOG] Order received <$message>")
        }
    }

    @Bean
    fun receiver(): LoggingReceiver = LoggingReceiver()

    @Bean
    fun listenerAdapter(receiver: LoggingReceiver): MessageListenerAdapter {
        return MessageListenerAdapter(receiver, "receiveMessage")
    }

    @Bean
    fun listenerContainer(
        connectionFactory: ConnectionFactory,
        listenerAdapter: MessageListenerAdapter
    ): SimpleMessageListenerContainer {
        val container = SimpleMessageListenerContainer()
        container.connectionFactory = connectionFactory
        container.setQueueNames(queueAkBtc, queueAkEth)
        container.setMessageListener(listenerAdapter)
        return container
    }

    // Producer configuration
    @Bean
    fun producer(rabbitTemplate: RabbitTemplate) = SendRunner(rabbitTemplate)

    class SendRunner(val rabbitTemplate: RabbitTemplate) : CommandLineRunner {
        @Throws(Exception::class)
        override fun run(vararg args: String) {

            while (true) {
                val currency = if( Random().nextInt() % 2 == 0) "eth" else "btc"

                println("Sending message for $currency")
                rabbitTemplate.convertAndSend(
                    akExchange,
                    "ak.${currency}",
                    "Buy:" + Random().nextInt().toString() +" "+ currency
                )
                Thread.sleep(2000)
            }
        }
    }
}