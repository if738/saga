package com.lesha.saga.repository

import com.lesha.saga.repository.entities.Offer
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface OrderRepository: JpaRepository<Offer, UUID>