package com.lesha.saga.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.lesha.saga.kafka.dto.OfferDto
import com.lesha.saga.service.ReservedBalanceService
import org.hibernate.annotations.common.util.impl.LoggerFactory
import org.springframework.stereotype.Component

@Component
class OfferCompletedListenerHandler(
    private val om: ObjectMapper,
    private val reservedBalanceService: ReservedBalanceService,
) : AbstractListenerHandler<OfferDto>(om) {

    private val log = LoggerFactory.logger(this::class.java)

    override val actionType: ActionType = ActionType.OFFER_COMPLETED

    override fun mapEventToEntity(event: EventDto): OfferDto {
        return om.readValue(event.payload, OfferDto::class.java)
    }

    override fun handle(dto: OfferDto) {
        try {
            reservedBalanceService.finishReservation(
                dto.customerId,
                dto.id,
                dto.valueTo!!,
                dto.currencyTo
            )
            log.debug("ExchangeOfferCompletedListenerHandler. processed=$dto")
        } catch (ex: Exception) {
            log.warn("ExchangeOfferCompletedListenerHandler. Failed. dto=$dto")
            throw ex
        }
    }
}
