package com.lesha.saga.service

import com.lesha.saga.kafka.KafkaProducer
import com.lesha.saga.kafka.consumer.ActionType
import com.lesha.saga.kafka.dto.OfferDto
import com.lesha.saga.repository.OrderRepository
import com.lesha.saga.repository.entities.Offer
import org.springframework.stereotype.Component
import java.util.*

@Component
class OfferService(
    private val repository: OrderRepository,
    private val kafkaProducer: KafkaProducer,
) {

    fun save(offer: Offer): Offer {
        repository.save(offer)
        val map = OfferDto.map(offer)
        for (i in 1..2) {
            kafkaProducer.sendMessage(ActionType.ORDER_CREATED, offer)
        }
        return offer
    }

    fun getOrThrow(id: UUID): Offer = repository.findById(id).orElseThrow { RuntimeException(" Can't find $id") }

}
