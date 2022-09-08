package com.lesha.saga.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.lesha.saga.kafka.dto.OfferDto
import com.lesha.saga.kafka.dto.ReservedBalanceDto
import com.lesha.saga.service.OfferService
import com.lesha.saga.service.enumerated.State
import org.hibernate.annotations.common.util.impl.LoggerFactory
import org.springframework.stereotype.Component

@Component
class BalanceReservedListenerHandler(
    private val om: ObjectMapper,
    private val OfferService: OfferService,
) : AbstractListenerHandler<OfferDto>(om) {

    override val actionType: ActionType = ActionType.CUSTOMER_BALANCE_RESERVED
    private val log = LoggerFactory.logger(this::class.java)

    override fun mapEventToEntity(event: EventDto): OfferDto {
        return om.readValue(event.payload, OfferDto::class.java)
    }

    override fun handle(dto: OfferDto) {
        try {
            if (dto.state != State.MONEY_RESERVED) {
                throw RuntimeException("BalanceReservedListenerHandler. Expected State.MONEY_RESERVED only")
            }
            OfferService.update(dto.map())
            log.debug("BalanceReservedListenerHandler. processed dto=$dto")
        } catch (ex: Exception) {
            log.debug("BalanceReservedListenerHandler. failed dto=$dto")
            throw ex
        }
    }

}
