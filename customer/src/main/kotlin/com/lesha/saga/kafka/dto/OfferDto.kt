package com.lesha.saga.kafka.dto

import com.lesha.saga.service.enumerated.State
import java.math.BigDecimal
import java.util.*

data class OfferDto(
    var id: UUID,
    var customerId: UUID,
    var reservedBalanceId: UUID? = null,
    var valueFrom: BigDecimal,
    var valueTo: BigDecimal? = null,
    var currencyFrom: String?,
    var currencyTo: String?,
    var state: State,
    var findBest: Boolean,
)
