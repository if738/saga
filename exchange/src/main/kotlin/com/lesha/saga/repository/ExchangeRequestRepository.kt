package com.lesha.saga.repository

import com.lesha.saga.repository.entities.ExchangeRequest
import com.lesha.saga.service.enumerated.State
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ExchangeRequestRepository : JpaRepository<ExchangeRequest, UUID> {
    fun findByOfferId(offerId: UUID): ExchangeRequest?
    fun findAllByState(state: State = State.MONEY_RESERVED): List<ExchangeRequest>
    fun existsByOfferId(offerId: UUID): Boolean
}