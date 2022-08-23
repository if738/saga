package com.lesha.saga.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.lesha.saga.kafka.dto.OfferDto
import com.lesha.saga.service.ReservedBalanceService
import org.springframework.stereotype.Component

//TODO FOR TESTING
@Component
class OrderCreatedListenerHandler(
    private val om: ObjectMapper,
    private val reservedBalanceService: ReservedBalanceService,
) : AbstractListenerHandler<OfferDto>(om) {

    override val actionType: ActionType = ActionType.ORDER_CREATED

    override fun mapEventToEntity(event: EventDto): OfferDto {
        return om.readValue(event.payload, OfferDto::class.java)
    }

    override fun handle(entity: OfferDto) {
        println("1 $entity")
        reservedBalanceService.reserve(entity.customerId, entity.id, entity.valueFrom)
    }

}
