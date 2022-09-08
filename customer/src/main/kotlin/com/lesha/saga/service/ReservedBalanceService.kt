package com.lesha.saga.service

import com.lesha.saga.repository.ReservedBalanceRepository
import com.lesha.saga.repository.entities.ReservedBalance
import com.lesha.saga.repository.entities.enums.Currency
import com.lesha.saga.service.enumerated.State
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.*

@Component
class ReservedBalanceService(
    private val reservedBalanceRepository: ReservedBalanceRepository,
    private val customerService: CustomerService,
    private val walletService: WalletService,
) {

    fun findById(reservedBalanceId: UUID): ReservedBalance {
        return reservedBalanceRepository.findByIdOrNull(reservedBalanceId)
            ?: throw RuntimeException("Can't find $reservedBalanceId")
    }

    fun findByOfferId(offerId: UUID): ReservedBalance {
        return reservedBalanceRepository.findByOfferId(offerId) ?: throw RuntimeException("Can't find $offerId")
    }

    fun finishReservation(customerId: UUID, offerId: UUID, value: BigDecimal, currency: Currency) {
        val reservedBalance = reservedBalanceRepository.findByOfferId(offerId)
            ?: throw RuntimeException("ReservedBalanceService.finishReservation can't find reserved balance by id=$customerId")
        reservedBalance.state = State.COMPLETED
        reservedBalanceRepository.save(reservedBalance)
        walletService.topUp(value, currency, customerId)
    }

    @Transactional(rollbackFor = [Exception::class], isolation = Isolation.REPEATABLE_READ)
    fun reserve(customerId: UUID, offerId: UUID, amount: BigDecimal, currency: Currency): ReservedBalance {
        val customer = customerService.getOrThrow(customerId)
        val wallet = walletService.getOrThrow(customerId, currency)
        if (wallet.balance < amount) {
            throw ArithmeticException("Not enough money customer=$customer reserve=$amount")
        }
        val subtractedBalance = wallet.balance.minus(amount)
        wallet.balance = subtractedBalance
        walletService.update(wallet)
        return reservedBalanceRepository.save(
            ReservedBalance(
                offerId = offerId,
                customerId = customerId,
                currency = currency,
                reservedBalance = amount,
                state = State.MONEY_RESERVED
            )
        )
    }

    @Transactional(rollbackFor = [Exception::class], isolation = Isolation.REPEATABLE_READ)
    fun handleCancellation(offerId: UUID): ReservedBalance {
        val reservedBalance = this.findByOfferId(offerId)
        val customer = reservedBalance.customerId?.let { customerService.getOrThrow(it) }
        if (reservedBalance.state.priority >= 3) {
            throw RuntimeException("Can cancel only with ${State.PENDING} ${State.MONEY_RESERVED}")
        }
        reservedBalance.state = State.CANCELED
        reservedBalanceRepository.save(reservedBalance)
        val wallet = walletService.getOrThrow(reservedBalance.customerId!!, currency = reservedBalance.currency!!)
        wallet.balance = wallet.balance.add(reservedBalance.reservedBalance)
        customer.let { customerService.update(it!!) }
        return reservedBalance
    }
}
