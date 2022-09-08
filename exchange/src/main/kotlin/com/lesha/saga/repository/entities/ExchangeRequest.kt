package com.lesha.saga.repository.entities

import com.lesha.saga.enums.Operation
import com.lesha.saga.kafka.dto.ExchangeRequestDto
import com.lesha.saga.repository.entities.enums.Currency
import com.lesha.saga.service.enumerated.State
import org.hibernate.annotations.GenericGenerator
import java.math.BigDecimal
import java.util.*
import javax.persistence.*

@Entity
data class ExchangeRequest(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    val id: UUID = UUID.randomUUID(),
    val offerId: UUID? = null,
    var valueFrom: BigDecimal? = BigDecimal.ZERO,
    var valueTo: BigDecimal? = BigDecimal.ZERO,
    @Enumerated(EnumType.STRING)
    val currencyFrom: Currency = Currency.USD,
    @Enumerated(EnumType.STRING)
    val currencyTo: Currency = Currency.BTC,
    @Enumerated(EnumType.STRING)
    var state: State = State.MONEY_RESERVED,
    val findBest: Boolean = false,
    @Enumerated(EnumType.STRING)
    val operation: Operation = Operation.SELL,
)