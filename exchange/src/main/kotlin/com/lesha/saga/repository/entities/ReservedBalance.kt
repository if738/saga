package com.lesha.saga.repository.entities

import com.lesha.saga.repository.entities.enums.Currency
import com.lesha.saga.service.enumerated.State
import org.hibernate.annotations.GenericGenerator
import java.math.BigDecimal
import java.util.*
import javax.persistence.*

@Entity
data class ReservedBalance(

    @Id
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    val id: UUID = UUID.randomUUID(),
    val customerId: UUID? = null,
    val offerId: UUID? = null,
    @Enumerated(EnumType.STRING)
    val currency: Currency = Currency.BTC,
    val reservedBalance: BigDecimal = BigDecimal.ZERO,
    @Enumerated(EnumType.STRING)
    var state: State = State.MONEY_RESERVED

)
