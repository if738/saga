package com.lesha.saga.service

import com.lesha.saga.kafka.KafkaProducer
import com.lesha.saga.repository.ExchangeRequestRepository
import com.lesha.saga.repository.entities.ExchangeRequest
import com.lesha.saga.service.enumerated.State
import org.apache.kafka.common.errors.InvalidRequestException
import org.hibernate.annotations.common.util.impl.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Component
class ExchangeRequestService(
    private val repository: ExchangeRequestRepository,
    private val kafkaProducer: KafkaProducer,
) {

    private val log = LoggerFactory.logger(this::class.java)

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun process(exchangeRequest: ExchangeRequest, stateTo: State): ExchangeRequest {
        val exchangeRequestExisted = repository.findByOfferId(exchangeRequest.offerId!!)
        if (exchangeRequestExisted == null) {
            val exchangeRequestCopy = exchangeRequest.copy()
            exchangeRequestCopy.state = stateTo
            val exchangeRequestResult = repository.save(exchangeRequestCopy)
            log.debug("ExchangeRequestService. Processed exchangeRequest=$exchangeRequestCopy")
            return exchangeRequestResult
        }
        if (exchangeRequestExisted.state.priority >= stateTo.priority) {
            throw RuntimeException("ExchangeRequestService. State sequence violated. stored state=${exchangeRequestExisted.state} " +
                    "requested state=$stateTo")
        }
            exchangeRequestExisted.state = stateTo
        val exchangeRequestResult = repository.save(exchangeRequestExisted)
        log.debug("ExchangeRequestService. Processed exchangeRequest=$exchangeRequest")
        return exchangeRequestResult
        }

    fun update(exchangeRequest: ExchangeRequest): ExchangeRequest {
        if (!repository.existsById(exchangeRequest.id))
            throw RuntimeException("ExchangeRequest with id=${exchangeRequest.id} is not exist, can't update")
        return repository.save(exchangeRequest)
    }

    fun saveAll(exchangeRequests: List<ExchangeRequest>): List<ExchangeRequest> {
        return repository.saveAll(exchangeRequests)
    }

    fun getAllWithMoneyReserved(): List<ExchangeRequest> {
        return repository.findAllByState()
    }

    fun getOrThrow(id: UUID): ExchangeRequest =
        repository.findById(id).orElseThrow { RuntimeException("Can't find $id") }

}
