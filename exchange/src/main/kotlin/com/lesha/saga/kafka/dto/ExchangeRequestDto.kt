package com.lesha.saga.kafka.dto

import com.lesha.saga.repository.entities.ExchangeRequest
import com.lesha.saga.service.enumerated.State
import java.math.BigDecimal
import java.util.*

data class ExchangeRequestDto(
    val id: UUID = UUID.randomUUID(),
    val offerId: UUID?,
    var valueFrom: BigDecimal = BigDecimal.ZERO,
    var valueTo: BigDecimal?,
    val currencyFrom: String?,
    val currencyTo: String?,
    var state: State = State.PENDING,
    val findBest: Boolean = false,
) {

    companion object {
        fun map(exchangeRequest: ExchangeRequest): ExchangeRequestDto {
            return ExchangeRequestDto(
                id = exchangeRequest.id,
                offerId = exchangeRequest.offerId,
                exchangeRequest.valueFrom,
                exchangeRequest.valueTo,
                exchangeRequest.currencyFrom,
                exchangeRequest.currencyTo,
                exchangeRequest.state,
                exchangeRequest.findBest,
            )
        }
    }

}