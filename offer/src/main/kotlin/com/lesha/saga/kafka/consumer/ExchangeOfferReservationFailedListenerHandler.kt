package com.lesha.saga.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.lesha.saga.kafka.KafkaProducer
import com.lesha.saga.kafka.dto.OfferDto
import com.lesha.saga.service.OfferService
import com.lesha.saga.service.enumerated.State
import org.hibernate.annotations.common.util.impl.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ExchangeOfferReservationFailedListenerHandler(
    private val om: ObjectMapper,
    private val offerService: OfferService,
    private val kafkaProducer: KafkaProducer,
) : AbstractListenerHandler<OfferDto>(om) {

    private val log = LoggerFactory.logger(this::class.java)

    override val actionType: ActionType = ActionType.EXCHANGE_OFFER_RESERVATION_FAILED

    override fun mapEventToEntity(event: EventDto): OfferDto {
        return om.readValue(event.payload, OfferDto::class.java)
    }

    override fun handle(dto: OfferDto) {
        try {
            dto.state = State.FAILED
            val offer = offerService.update(dto.map())
            kafkaProducer.sendMessage(ActionType.OFFER_ROLLBACK, OfferDto.map(offer))
            log.debug("ExchangeOfferReservationFailedListenerHandler. Processed. dto=$dto")
        } catch (ex: Exception) {
            log.warn("ExchangeOfferReservationFailedListenerHandler. Failed. dto=$dto")
            dto.state = State.FAILED
            kafkaProducer.sendMessage(ActionType.OFFER_ROLLBACK_FAILED, dto)
            throw ex
        }
    }
}
