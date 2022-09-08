package com.lesha.saga.kafka.dto

import com.lesha.saga.repository.entities.ReservedBalance
import com.lesha.saga.service.enumerated.State
import java.math.BigDecimal
import java.util.*

data class ReservedBalanceDto(
    var id: UUID,
    var customerId: UUID,
    var offerId: UUID,
    var currency: String? = null,
    var reservedBalance: BigDecimal = BigDecimal.ZERO,
    var state: State = State.PENDING,
) {

    companion object {
        fun map(reservedBalance: ReservedBalance): ReservedBalanceDto {
            return ReservedBalanceDto(
                reservedBalance.id,
                reservedBalance.customerId
                    ?: throw RuntimeException("ReservedBalanceDto.map.reservedBalance.customerId required"),
                reservedBalance.offerId
                    ?: throw RuntimeException("ReservedBalanceDto.map.reservedBalance.offerId required"),
                reservedBalance.currency?.name,
                reservedBalance.reservedBalance,
                reservedBalance.state,
            )
        }
    }

}