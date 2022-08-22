package com.lesha.saga.service

import com.lesha.saga.repository.CustomerRepository
import com.lesha.saga.repository.entities.Customer
import org.springframework.stereotype.Component
import java.util.*

@Component
class CustomerService(private val repository: CustomerRepository) {

    fun save(customer: Customer): Customer = repository.save(customer)
    fun getOrThrow(id: UUID): Customer = repository.findById(id).orElseThrow { RuntimeException(" Can't find $id") }

}
