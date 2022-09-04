package com.lesha.saga.service

import com.lesha.saga.kafka.KafkaProducer
import com.lesha.saga.kafka.consumer.ActionType
import com.lesha.saga.repository.ExchangeRequestRepository
import com.lesha.saga.repository.entities.ExchangeRequest
import com.lesha.saga.service.enumerated.State
import org.hibernate.annotations.common.util.impl.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Component
class ExchangeRequestService(
    private val repository: ExchangeRequestRepository,
    private val kafkaProducer: KafkaProducer,
) {

    private val log = LoggerFactory.logger(this::class.java)

    @Transactional
    fun process(exchangeRequest: ExchangeRequest) {
        val exchangeRequestResult = repository.findByOfferId(exchangeRequest.offerId!!)
        if (exchangeRequestResult != null) {
            if (exchangeRequestResult.state == State.PENDING && exchangeRequest.state == State.MONEY_RESERVED) {
                exchangeRequestResult.state = State.MONEY_RESERVED
                repository.save(exchangeRequestResult)
                log.debug("ExchangeRequestService. created exchangeRequest=$exchangeRequest")
                kafkaProducer.sendMessage(ActionType.EXCHANGE_OFFER_RESERVED, exchangeRequest)
                return
            }
            if (exchangeRequest.state == State.MONEY_RESERVED) {
                log.debug("ExchangeRequestService. skipped exchangeRequest=$exchangeRequest")
                return
            }
        }
        repository.save(exchangeRequest)
        kafkaProducer.sendMessage(ActionType.EXCHANGE_OFFER_PENDING, exchangeRequest)
        log.debug("ExchangeRequestService. created exchangeRequest=$exchangeRequest")
    }

    fun update(exchangeRequest: ExchangeRequest): ExchangeRequest {
        if (!repository.existsById(exchangeRequest.id))
            throw RuntimeException("ExchangeRequest with id=${exchangeRequest.id} is not exist, can't update")
        return repository.save(exchangeRequest)
    }

    fun getOrThrow(id: UUID): ExchangeRequest =
        repository.findById(id).orElseThrow { RuntimeException("Can't find $id") }

}
