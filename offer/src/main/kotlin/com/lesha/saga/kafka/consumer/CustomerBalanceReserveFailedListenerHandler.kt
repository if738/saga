package com.lesha.saga.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.lesha.saga.kafka.dto.CustomerDto
import com.lesha.saga.kafka.dto.OfferDto
import com.lesha.saga.service.CustomerService
import com.lesha.saga.service.OfferService
import com.lesha.saga.service.enumerated.State
import org.springframework.stereotype.Component

//TODO MOCKED
@Component
class CustomerBalanceReserveFailedListenerHandler(
    private val om: ObjectMapper,
    private val offerService: OfferService,
) : AbstractListenerHandler<OfferDto>(om) {

    override val actionType: ActionType = ActionType.CUSTOMER_BALANCE_RESERVE_FAILED

    override fun mapEventToEntity(event: EventDto): OfferDto {
        return om.readValue(event.payload, OfferDto::class.java)
    }

    override fun handle(dto: OfferDto) {
        val offer = dto.map()
        offer.state = State.FAILED
        offerService.update(offer)
    }

}
