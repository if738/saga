package com.lesha.saga.kafka.dto

import com.lesha.saga.repository.entities.enums.Operation
import com.lesha.saga.service.enumerated.State
import java.math.BigDecimal
import java.util.*

data class ExchangeRequestDto(
    val id: UUID = UUID.randomUUID(),
    val offerId: UUID,
    var valueFrom: BigDecimal? = BigDecimal.ZERO,
    var valueTo: BigDecimal? = BigDecimal.ZERO,
    val currencyFrom: String,
    val currencyTo: String,
    var state: State = State.MONEY_RESERVED,
    val findBest: Boolean = false,
    val operation: Operation
)
