package com.lesha.saga.repository

import com.lesha.saga.repository.entities.Wallet
import com.lesha.saga.repository.entities.enums.Currency
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface WalletRepository : JpaRepository<Wallet, UUID> {
    fun findByCustomerIdAndCurrency(customerId: UUID, currency: Currency): Wallet?
}