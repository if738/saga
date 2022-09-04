package com.lesha.saga.repository.entities

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
    var reservedBalanceId: UUID? = null,
    var valueFrom: BigDecimal = BigDecimal.ZERO,
    var valueTo: BigDecimal? = BigDecimal.ZERO,
    val currencyFrom: String? = null,
    val currencyTo: String? = null,
    @Enumerated(EnumType.STRING)
    var state: State = State.PENDING,
    val findBest: Boolean = false,
)
