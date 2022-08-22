package com.lesha.saga.service

import com.lesha.saga.kafka.KafkaProducer
import com.lesha.saga.kafka.consumer.ActionType
import com.lesha.saga.kafka.consumer.EventDto
import com.lesha.saga.kafka.dto.ReservedBalanceDto
import com.lesha.saga.repository.ReservedBalanceRepository
import com.lesha.saga.repository.entities.ReservedBalance
import com.lesha.saga.service.entity.State
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

    fun create(reservedBalance: ReservedBalance): ReservedBalance {
        reservedBalance.customerId?.let { customerService.getOrThrow(it) }
        return reservedBalanceRepository.save(reservedBalance)
    }


    @Transactional(rollbackOn = [Exception::class])
    fun reserve(customerId: UUID, orderId: UUID, reserve: BigDecimal): ReservedBalance {
        val customer = customerService.getOrThrow(customerId)
        val balance = customer.balance
        if (balance < reserve) {
            throw RuntimeException("Not enough money customer=$customer reserve=$reserve")
        }
        val subtractedBalance = balance.minus(reserve)
        customer.balance = subtractedBalance
        customerService.save(customer)
        return reservedBalanceRepository.save(
            ReservedBalance(
                orderId = orderId,
                customerId = customerId,
                currency = customer.currency,
                reservedBalance = reserve,
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
