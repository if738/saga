package com.lesha.saga.service

import com.lesha.saga.kafka.KafkaProducer
import com.lesha.saga.kafka.consumer.ActionType
import com.lesha.saga.repository.ReservedBalanceRepository
import com.lesha.saga.repository.entities.ReservedBalance
import com.lesha.saga.service.enumerated.State
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.UUID
import javax.transaction.Transactional

@Component
class ReservedBalanceService(
    private val reservedBalanceRepository: ReservedBalanceRepository,
    private val customerService: CustomerService,
    private val kafkaProducer: KafkaProducer
) {

    fun getOrThrow(reservedBalanceId: UUID): ReservedBalance {
        return reservedBalanceRepository.findById(reservedBalanceId)
            .orElseThrow { throw RuntimeException("Can't find $reservedBalanceId") }
    }

    @Transactional(rollbackOn = [Exception::class])
    fun reserve(customerId: UUID, offerId: UUID, count: BigDecimal): ReservedBalance {
        val customer = customerService.getOrThrow(customerId)
        val balance = customer.balance
        if (balance < count) {
            throw RuntimeException("Not enough money customer=$customer reserve=$count")
        }
        val subtractedBalance = balance.minus(count)
        customer.balance = subtractedBalance
        customerService.save(customer)
        return reservedBalanceRepository.save(
            ReservedBalance(
                offerId = offerId,
                customerId = customerId,
                currency = customer.currency,
                reservedBalance = count,
                state = State.PENDING
            )
        )
    }

    fun cancel(reservedBalanceId: UUID): ReservedBalance {
        val reservedBalance = this.getOrThrow(reservedBalanceId)
        reservedBalance.state = State.CANCEL_PENDING
        reservedBalanceRepository.save(reservedBalance)
        kafkaProducer.sendMessage(ActionType.CUSTOMER_BALANCE_RESERVED, reservedBalance)
        return reservedBalance
    }

    @Transactional(rollbackOn = [Exception::class])
    fun handleCancellation(reservedBalanceId: UUID): ReservedBalance {
        val reservedBalance = this.getOrThrow(reservedBalanceId)
        val customer = reservedBalance.customerId?.let { customerService.getOrThrow(it) }

        if (reservedBalance.state != State.PENDING) {
            throw RuntimeException("Can cancel only with ${State.PENDING}")
        }
        reservedBalance.state = State.CANCELED
        reservedBalanceRepository.save(reservedBalance)
        customer?.balance = customer?.balance?.add(reservedBalance.reservedBalance)!!
        customer.let { customerService.save(it) }
        return reservedBalance
    }
}
