package com.lesha.saga.service

import com.lesha.saga.kafka.KafkaProducer
import com.lesha.saga.kafka.consumer.ActionType
import com.lesha.saga.kafka.dto.CustomerDto
import com.lesha.saga.repository.CustomerRepository
import com.lesha.saga.repository.entities.Customer
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.util.*

@Component
class CustomerService(
    private val repository: CustomerRepository,
    private val producer: KafkaProducer,
) {

    fun create(customer: Customer): Customer {
        val customerResult  = repository.save(customer)
        val dto = CustomerDto.map(customerResult)
        producer.sendMessage(ActionType.CUSTOMER_CREATED, dto)
        return customerResult
    }

    fun update(customer: Customer): Customer {
        if (!repository.existsById(customer.id))
            throw RuntimeException("Customer with id=${customer.id} is not exist, can't update")
        return repository.save(customer)
    }

    fun getOrThrow(id: UUID): Customer = repository.findByIdOrNull(id) ?: throw RuntimeException("Can't find $id")

}
