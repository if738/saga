package com.lesha.saga.repository

import com.lesha.saga.repository.entities.Customer
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CustomerRepository: JpaRepository<Customer, UUID>