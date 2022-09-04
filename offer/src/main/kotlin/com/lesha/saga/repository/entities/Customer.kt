package com.lesha.saga.repository.entities

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Customer(
    @Id
    val id: UUID = UUID.randomUUID(),
    val name: String? = null,
)