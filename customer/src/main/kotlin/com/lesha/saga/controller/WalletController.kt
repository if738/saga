package com.lesha.saga.controller

import com.lesha.saga.repository.entities.enums.Currency
import com.lesha.saga.service.WalletService
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.util.*

@RestController
@RequestMapping("/wallet")
class WalletController(private val service: WalletService) {

    @PutMapping
    fun topUp(
        @RequestParam
        amount: BigDecimal,
        @RequestParam
        currency: Currency,
        @RequestParam
        customerId: UUID
    ) = service.topUp(amount, currency, customerId)

    @GetMapping
    fun get(
        @RequestParam
        id: UUID
    ) = service.getOrThrow(id)
}
