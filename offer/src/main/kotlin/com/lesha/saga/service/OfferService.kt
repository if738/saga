package com.lesha.saga.service

import com.lesha.saga.kafka.KafkaProducer
import com.lesha.saga.kafka.consumer.ActionType
import com.lesha.saga.kafka.dto.OfferDto
import com.lesha.saga.repository.OfferRepository
import com.lesha.saga.repository.entities.Offer
import com.lesha.saga.service.enumerated.State
import org.springframework.data.repository.findByIdOrNull
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
        if (offer.valueTo == BigDecimal.ZERO && !offer.findBest) throw RuntimeException("valueTo is zero and findBest is false, can be mistake")
        if (!customerService.isExist(offer.customerId!!)) throw RuntimeException("Can't find ${offer.customerId}")
        val offerResult = repository.save(offer)
        val dto = OfferDto.map(offerResult)
        kafkaProducer.sendMessage(ActionType.OFFER_CREATED, dto)
        return offer
    }

    fun update(offer: Offer): Offer {
        if (!repository.existsById(offer.id)) throw RuntimeException("Offer with id=${offer.id} is not exist, can't update")
        return repository.save(offer)
    }

    fun getOrThrow(id: UUID): Offer = repository.findByIdOrNull(id) ?: throw RuntimeException("Can't find $id")

    fun update(id: UUID, state: State): Offer {
        val offer =
            repository.findByIdOrNull(id) ?: throw RuntimeException("OfferService.cancel can't find offer by id=$id")
        offer.state = state
        return repository.save(offer)
    }

    fun update(id: UUID, state: State, value: BigDecimal): Offer {
        val offer =
            repository.findByIdOrNull(id) ?: throw RuntimeException("OfferService.cancel can't find offer by id=$id")
        offer.state = state
        offer.valueTo = value
        return repository.save(offer)
    }

    fun cancelPending(id: UUID) {
        val offer =
            repository.findByIdOrNull(id) ?: throw RuntimeException("OfferService.cancel can't find offer by id=$id")
        if (offer.state.priority < State.CANCEL_PENDING.priority) {
            offer.state = State.CANCEL_PENDING
            repository.save(offer)
            kafkaProducer.sendMessage(ActionType.OFFER_CANCELLATION_PENDING, OfferDto.map(offer))
        } else {
            throw RuntimeException("OfferService.cancelPending State sequence violated. stored state=${offer.state} " +
                    "requested state=${State.CANCEL_PENDING}")
        }
    }
}
