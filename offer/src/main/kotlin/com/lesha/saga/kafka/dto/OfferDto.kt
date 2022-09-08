package com.lesha.saga.kafka.dto

import com.lesha.saga.repository.entities.Offer
import com.lesha.saga.repository.entities.enums.Currency
import com.lesha.saga.repository.entities.enums.Operation
import com.lesha.saga.service.enumerated.State
import java.math.BigDecimal
import java.util.*

data class OfferDto(
    var id: UUID,
    var customerId: UUID,
    var reservedBalanceId: UUID? = null,
    var valueFrom: BigDecimal?= null,
    var valueTo: BigDecimal?,
    var currencyFrom: String,
    var currencyTo: String,
    var state: State,
    var findBest: Boolean,
    val operation: Operation = Operation.BUY,
) {
    fun map(): Offer {
        return Offer(
            this.id,
            this.customerId ?: throw RuntimeException("OfferDto.map.customerId required"),
            this.reservedBalanceId,
            this.valueFrom,
            this.valueTo,
            this.currencyFrom.let { Currency.valueOf(it) },
            this.currencyTo.let { Currency.valueOf(it) },
            this.state,
            this.findBest,
            this.operation,
        )
    }

    companion object {
        fun map(offer: Offer): OfferDto {
            return OfferDto(
                offer.id,
                offer.customerId ?: throw RuntimeException("OfferDto.map.customerId required"),
                offer.reservedBalanceId,
                offer.valueFrom,
                offer.valueTo,
                offer.currencyFrom.name,
                offer.currencyTo.name,
                offer.state,
                offer.findBest,
                offer.operation,
            )
        }
    }
}
