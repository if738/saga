package com.lesha.saga.spam

import com.fasterxml.jackson.databind.ObjectMapper
import com.lesha.saga.repository.entities.Offer
import com.lesha.saga.service.OfferService
import com.lesha.saga.service.enumerated.State
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.*

@EnableScheduling
@Component
class Spammer(private val offerService: OfferService, private val om: ObjectMapper) {

    @Scheduled(fixedDelay = 100000L)
    fun init() {
        val offer = Offer(customerId = UUID.fromString("a4dc551a-73a1-4b16-b8c8-5ad994e554a2"),
            valueFrom = BigDecimal.ONE,
            currencyFrom = "USD",
            currencyTo = "USD",
            state = State.PENDING, findBest = true)
        offerService.save(offer)
    }
}
