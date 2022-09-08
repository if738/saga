package com.lesha.saga.service

import com.lesha.saga.repository.CustomerRepository
import com.lesha.saga.repository.WalletRepository
import com.lesha.saga.repository.entities.Wallet
import com.lesha.saga.repository.entities.enums.Currency
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.*

@Component
class WalletService(
    private val repository: WalletRepository,
    private val customerRepository: CustomerRepository,
) {

    fun topUp(amount: BigDecimal, currency: Currency, customerId: UUID): Wallet {
        if (!customerRepository.existsById(customerId)) {
            throw RuntimeException("Customer with id=${customerId} is not exist, can't top up")
        }
        val wallet = repository.findByCustomerIdAndCurrency(customerId, currency)
            ?: return repository.save(Wallet(customerId = customerId, currency = currency, balance = amount))

        wallet.balance = wallet.balance.add(amount)
        repository.save(wallet)
        return wallet
    }

    fun getOrThrow(id: UUID): Wallet = repository.findByIdOrNull(id) ?: throw RuntimeException("Can't find $id")

    fun getOrThrow(customerId: UUID, currency: Currency): Wallet {
        return repository.findByCustomerIdAndCurrency(customerId, currency)
            ?: throw RuntimeException("Wallet with id=${customerId} and currency=$currency is not exist")
    }

    fun update(wallet: Wallet): Wallet {
        if (!customerRepository.existsById(wallet.customerId)) {
            throw RuntimeException("Wallet with id=${wallet.id} is not exist")
        }
        return repository.save(wallet)
    }

}
