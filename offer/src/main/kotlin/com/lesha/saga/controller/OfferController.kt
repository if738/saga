package com.lesha.saga.controller

import com.lesha.saga.repository.entities.Offer
import com.lesha.saga.service.OfferService
import com.lesha.saga.service.enumerated.State
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/offer")
class OfferController(private val service: OfferService) {

    @PostMapping
    fun create(@RequestBody entity: Offer): Offer {
        entity.state = State.PENDING
        entity.reservedBalanceId = null
        return service.save(entity)
    }

    @PutMapping("/cancel")
    fun cancel(@RequestParam id: UUID) {
        service.cancelPending(id)
    }

    @GetMapping
    fun get(@RequestParam id: UUID) =
        service.getOrThrow(id)
}
