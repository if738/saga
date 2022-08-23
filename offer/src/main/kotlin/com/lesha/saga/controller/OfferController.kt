package com.lesha.saga.controller

import com.lesha.saga.repository.entities.Offer
import com.lesha.saga.service.OfferService
import com.lesha.saga.service.enumerated.State
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/customer")
class OfferController(private val service: OfferService) {

    @PostMapping
    fun create(@RequestBody entity: Offer) {
        entity.state = State.PENDING
        service.save(entity)
    }

    @GetMapping
    fun get(@RequestParam id: UUID) =
        service.getOrThrow(id)
}
