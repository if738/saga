package com.lesha.saga.controller

import com.lesha.saga.repository.entities.ReservedBalance
import com.lesha.saga.service.ReservedBalanceService
import com.lesha.saga.service.entity.State
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/reserved-balance")
class ReservedBalanceController(private val service: ReservedBalanceService) {

    @PostMapping
    fun create(@RequestBody entity: ReservedBalance): ReservedBalance {
        entity.state = State.PENDING
         return service.create(entity)
    }

    @GetMapping
    fun get(@RequestParam id: UUID): ReservedBalance = service.getOrThrow(id)

    @PutMapping("/cancel")
    fun cancel(@RequestParam id: UUID): ReservedBalance {
        return service.cancel(id)
    }

}
