package com.lesha.saga.repository.entities

import com.lesha.saga.repository.entities.enums.Currency
import org.hibernate.annotations.GenericGenerator
import java.math.BigDecimal
import java.util.*
import javax.persistence.*

@Entity
data class Customer(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    val id: UUID = UUID.randomUUID(),
    val name: String? = null,
    @Enumerated(EnumType.STRING)
    val currency: Currency? = null,
    var balance: BigDecimal = BigDecimal.ZERO
)
