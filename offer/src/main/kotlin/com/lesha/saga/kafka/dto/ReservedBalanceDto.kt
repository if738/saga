package com.lesha.saga.kafka.dto

import com.lesha.saga.service.enumerated.State
import java.math.BigDecimal
import java.util.*

data class ReservedBalanceDto(

    var id: UUID,
    var customerId: UUID,
    var orderId: UUID,
    var currency: String? = null,
    var reservedBalance: BigDecimal = BigDecimal.ZERO,
    var state: State = State.PENDING,
    )