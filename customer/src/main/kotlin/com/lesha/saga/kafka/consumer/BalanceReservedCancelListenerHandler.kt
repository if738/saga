package com.lesha.saga.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.lesha.saga.kafka.dto.ReservedBalanceDto
import com.lesha.saga.service.ReservedBalanceService
import org.springframework.stereotype.Component

//TODO FOR TESTING
@Component
class BalanceReservedCancelListenerHandler(
    private val om: ObjectMapper,
    private val balaReservedBalanceService: ReservedBalanceService
) : AbstractListenerHandler<ReservedBalanceDto>(om) {

    override val actionType: ActionType = ActionType.CUSTOMER_BALANCE_RESERVE_CANCEL

    override fun mapEventToEntity(event: EventDto): ReservedBalanceDto {
        return om.readValue(event.payload, ReservedBalanceDto::class.java)
    }

    override fun handle(entity: ReservedBalanceDto) {
        println("Handled woohoo, go sleep! $entity")
    }

}
