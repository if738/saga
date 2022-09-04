package com.lesha.saga.controller

import com.lesha.saga.service.ReservedBalanceService
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.util.*

@RestController
@RequestMapping("/reserved-balance")
class ReservedBalanceController(private val service: ReservedBalanceService) {

    @PostMapping("/reserve")
    fun create(@RequestParam customerId: UUID, offerId: UUID, count: BigDecimal) =
        service.reserve(customerId, offerId, count)

    @GetMapping
    fun get(@RequestParam id: UUID) =
        service.getOrThrow(id)

    @PutMapping("/cancel")
    fun cancel(@RequestParam id: UUID) =
        service.cancel(id)
}
