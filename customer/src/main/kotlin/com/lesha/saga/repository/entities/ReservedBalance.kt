package com.lesha.saga.repository.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.lesha.saga.service.entity.State
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
    val orderId: UUID? = null,
    val currency: String? = null,
    val reservedBalance: BigDecimal = BigDecimal.ZERO,
    @Enumerated(EnumType.STRING)
    var state: State = State.PENDING

)
