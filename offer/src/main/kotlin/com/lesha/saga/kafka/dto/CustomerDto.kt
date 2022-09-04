package com.lesha.saga.kafka.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.lesha.saga.repository.entities.Customer
import java.math.BigDecimal
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class CustomerDto(
    val id: UUID,
    val name: String,
) {

    fun map(): Customer {
        return Customer(
            this.id,
            this.name,
        )
    }

    companion object {
        fun map(customer: Customer): CustomerDto {
            return CustomerDto(
                customer.id,
                customer.name ?: throw RuntimeException("CustomerDto.map.customer.name can't be null"),
            )
        }
    }
}