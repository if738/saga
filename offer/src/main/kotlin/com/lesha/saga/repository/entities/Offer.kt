package com.lesha.saga.repository.entities

import com.lesha.saga.repository.entities.enums.Currency
import com.lesha.saga.repository.entities.enums.Operation
import com.lesha.saga.service.enumerated.State
import org.hibernate.annotations.GenericGenerator
import java.math.BigDecimal
import java.util.*
import javax.persistence.*

@Entity
data class Offer(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    val id: UUID = UUID.randomUUID(),
    val customerId: UUID? = null,
    //TODO how to hide it from Controller?
    var reservedBalanceId: UUID? = null,
    var valueFrom: BigDecimal? = BigDecimal.ZERO,
    var valueTo: BigDecimal? = BigDecimal.ZERO,
    @Enumerated(EnumType.STRING)
    val currencyFrom: Currency = Currency.USD,
    @Enumerated(EnumType.STRING)
    val currencyTo: Currency = Currency.BTC,
    @Enumerated(EnumType.STRING)
    var state: State = State.PENDING,
    val findBest: Boolean = false,
    @Enumerated(EnumType.STRING)
    val operation: Operation = Operation.BUY,
)
