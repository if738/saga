package com.lesha.saga.kafka.dto

import com.lesha.saga.enums.Operation
import com.lesha.saga.repository.entities.ExchangeRequest
import com.lesha.saga.repository.entities.enums.Currency
import com.lesha.saga.service.enumerated.State
import java.math.BigDecimal
import java.util.*

data class OfferDto(
    var id: UUID,
    var customerId: UUID,
    var reservedBalanceId: UUID? = null,
    var valueFrom: BigDecimal?,
    var valueTo: BigDecimal?,
    var currencyFrom: String,
    var currencyTo: String,
    var state: State,
    var findBest: Boolean,
    var operation: Operation,
) {

    fun map(): ExchangeRequest {
        return ExchangeRequest(
            id = UUID.randomUUID(),
            offerId = this.id,
            this.valueFrom,
            this.valueTo,
            this.currencyFrom.let { Currency.valueOf(it) },
            this.currencyTo.let { Currency.valueOf(it) },
            this.state,
            this.findBest,
        )
    }

}
