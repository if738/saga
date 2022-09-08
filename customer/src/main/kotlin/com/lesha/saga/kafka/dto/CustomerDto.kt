package com.lesha.saga.kafka.dto

import com.lesha.saga.repository.entities.Customer
import java.math.BigDecimal
import java.util.*

data class CustomerDto(
    val id: UUID,
    val name: String,
) {
    companion object {
        fun map(customer: Customer): CustomerDto {
            return CustomerDto(
                customer.id,
                customer.name ?: throw RuntimeException("CustomerDto.map.customer.name can't be null")
            )
        }
    }
}