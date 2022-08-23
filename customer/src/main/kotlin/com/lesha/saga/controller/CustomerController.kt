package com.lesha.saga.controller

import com.lesha.saga.repository.entities.Customer
import com.lesha.saga.service.CustomerService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/customer")
class CustomerController(private val service: CustomerService) {

    @PostMapping
    fun create(@RequestBody entity: Customer) =
        service.save(entity)

    @GetMapping
    fun get(@RequestParam id: UUID) =
        service.getOrThrow(id)
}
