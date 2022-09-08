package com.lesha.saga.listener

import com.lesha.saga.enums.Operation
import com.lesha.saga.repository.entities.enums.Currency
import java.math.BigDecimal

data class Asset(
    val operation: Operation,
    val currencyFrom: Currency,
    val currencyTo: Currency,
    val rate: BigDecimal,
)
