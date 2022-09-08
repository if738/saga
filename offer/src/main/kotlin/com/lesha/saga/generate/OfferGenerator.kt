package com.lesha.saga.generate

import com.lesha.saga.repository.CustomerRepository
import com.lesha.saga.repository.entities.Offer
import com.lesha.saga.repository.entities.enums.Currency
import com.lesha.saga.repository.entities.enums.Operation
import com.lesha.saga.service.OfferService
import com.lesha.saga.service.enumerated.State
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.math.BigDecimal
import kotlin.random.Random

@Component
class OfferGenerator(
    private val customerRepository: CustomerRepository, private val offerService: OfferService
) {

    @Scheduled(fixedDelay = 200L)
    fun generate() {
        val customers = customerRepository.findAll().toList()
        customers.forEach { customer ->
            Operation.values().toMutableList().shuffled().forEach { operation ->
                val usdToBtc = Offer(
                    customerId = customer.id,
                    valueFrom = BigDecimal(Random.nextDouble(18000.0, 22000.0)).divide(BigDecimal.TEN),
                    valueTo = BigDecimal(Random.nextDouble(0.000045, 0.000055)).divide(BigDecimal.TEN),
                    currencyFrom = Currency.USD,
                    currencyTo = Currency.BTC,
                    state = State.PENDING,
                    findBest = false,
                    operation = operation
                )
                offerService.save(usdToBtc)
                val btcToUsd = Offer(
                    customerId = customer.id,
                    valueFrom = BigDecimal(Random.nextDouble(0.000045, 0.000055)).divide(BigDecimal.TEN),
                    valueTo = BigDecimal(Random.nextDouble(18000.0, 22000.0)).divide(BigDecimal.TEN),
                    currencyFrom = Currency.BTC,
                    currencyTo = Currency.USD,
                    state = State.PENDING,
                    findBest = false,
                    operation = operation
                )
                offerService.save(btcToUsd)
                val usdToBtcBest = Offer(
                    customerId = customer.id,
                    valueFrom = BigDecimal(Random.nextDouble(18000.0, 22000.0)).divide(BigDecimal.TEN),
                    valueTo = BigDecimal(Random.nextDouble(0.000045, 0.000055)).divide(BigDecimal.TEN),
                    currencyFrom = Currency.USD,
                    currencyTo = Currency.BTC,
                    state = State.PENDING,
                    findBest = true,
                    operation = operation
                )
                offerService.save(usdToBtcBest)
                val btcToUsdBest = Offer(
                    customerId = customer.id,
                    valueFrom = BigDecimal(Random.nextDouble(0.000045, 0.000055)).divide(BigDecimal.TEN),
                    valueTo = BigDecimal(Random.nextDouble(18000.0, 22000.0)).divide(BigDecimal.TEN),
                    currencyFrom = Currency.BTC,
                    currencyTo = Currency.USD,
                    state = State.PENDING,
                    findBest = true,
                    operation = operation
                )
                offerService.save(btcToUsdBest)
            }
        }
    }
}
