package com.lesha.saga.kafka.dto

import com.lesha.saga.repository.entities.enums.Currency
import com.lesha.saga.repository.entities.enums.Operation
import com.lesha.saga.service.enumerated.State
import java.math.BigDecimal
import java.util.*

data class OfferDto(
    var id: UUID,
    var customerId: UUID,
    var reservedBalanceId: UUID? = null,
    var valueFrom: BigDecimal,
    var valueTo: BigDecimal?,
    var currencyFrom: Currency,
    var currencyTo: Currency,
    var state: State,
    var findBest: Boolean,
    var operation: Operation,
)
