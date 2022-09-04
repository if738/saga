package com.lesha.saga.service

import com.lesha.saga.kafka.KafkaProducer
import com.lesha.saga.kafka.consumer.ActionType
import com.lesha.saga.kafka.dto.OfferDto
import com.lesha.saga.repository.OfferRepository
import com.lesha.saga.repository.entities.Customer
import com.lesha.saga.repository.entities.Offer
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.*

@Component
class OfferService(
    private val repository: OfferRepository,
    private val kafkaProducer: KafkaProducer,
    private val customerService: CustomerService,
) {

    fun save(offer: Offer): Offer {
        if (offer.valueFrom == BigDecimal.ZERO) throw RuntimeException("valueFrom can't be null or zero")
        if(offer.valueTo == BigDecimal.ZERO && !offer.findBest) throw RuntimeException("valueTo is zero and findBest is false, can be mistake")
        if (!customerService.isExist(offer.customerId!!)) throw RuntimeException("Can't find ${offer.customerId}")
        val offerResult = repository.save(offer)
        val dto = OfferDto.map(offerResult)
        kafkaProducer.sendMessage(ActionType.OFFER_CREATED, dto)
        return offer
    }

    fun update(offer: Offer): Offer {
        if (!repository.existsById(offer.id))
            throw RuntimeException("Offer with id=${offer.id} is not exist, can't update")
        return repository.save(offer)
    }

    fun getOrThrow(id: UUID): Offer = repository.findById(id).orElseThrow { RuntimeException("Can't find $id") }

}
