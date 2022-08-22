package com.lesha.saga.spam

import com.fasterxml.jackson.databind.ObjectMapper
import com.lesha.saga.kafka.KafkaProducer
import com.lesha.saga.kafka.consumer.ActionType
import com.lesha.saga.kafka.dto.ReservedBalanceDto
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*

@EnableScheduling
@Component
class Spammer(private val producer: KafkaProducer, private val om: ObjectMapper) {

    @Scheduled(fixedDelay = 5555)
    fun init() {
        producer.sendMessage(
            ActionType.CUSTOMER_BALANCE_RESERVE_CANCEL,
            ReservedBalanceDto(id = UUID.randomUUID(), customerId = UUID.randomUUID(), orderId = UUID.randomUUID())
        )
        producer.sendMessage(
            ActionType.CUSTOMER_BALANCE_RESERVED,
            ReservedBalanceDto(id = UUID.randomUUID(), customerId = UUID.randomUUID(), orderId = UUID.randomUUID())
        )
    }
}
