package com.lesha.saga.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.lesha.saga.kafka.dto.ReservedBalanceDto
import org.springframework.stereotype.Component

//TODO MOCKED
@Component
class BalanceReservedCancelListenerHandler(
    private val om: ObjectMapper,
) : AbstractListenerHandler<ReservedBalanceDto>(om) {

    override val actionType: ActionType = ActionType.CUSTOMER_BALANCE_RESERVE_CANCEL

    override fun mapEventToEntity(event: EventDto): ReservedBalanceDto {
        return om.readValue(event.payload, ReservedBalanceDto::class.java)
    }

    override fun handle(entity: ReservedBalanceDto) {
        println("WOOHOO, we can see BalanceReservedCancelListenerHandler events")
    }

}
