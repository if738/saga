package com.lesha.saga.repository

import com.lesha.saga.repository.entities.ReservedBalance
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ReservedBalanceRepository : JpaRepository<ReservedBalance, UUID>{
    fun findByOfferId(offerId: UUID): ReservedBalance?
}