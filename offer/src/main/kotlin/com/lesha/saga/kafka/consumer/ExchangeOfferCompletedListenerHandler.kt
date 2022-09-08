package com.lesha.saga.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.lesha.saga.kafka.KafkaProducer
import com.lesha.saga.kafka.dto.ExchangeRequestDto
import com.lesha.saga.kafka.dto.OfferDto
import com.lesha.saga.kafka.dto.ReservedBalanceDto
import com.lesha.saga.service.OfferService
import com.lesha.saga.service.enumerated.State
import org.hibernate.annotations.common.util.impl.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ExchangeOfferCompletedListenerHandler(
    private val om: ObjectMapper,
    private val offerService: OfferService,
    private val kafkaProducer: KafkaProducer,
) : AbstractListenerHandler<ExchangeRequestDto>(om) {

    private val log = LoggerFactory.logger(this::class.java)

    override val actionType: ActionType = ActionType.EXCHANGE_OFFER_COMPLETED

    override fun mapEventToEntity(event: EventDto): ExchangeRequestDto {
        return om.readValue(event.payload, ExchangeRequestDto::class.java)
    }

    override fun handle(dto: ExchangeRequestDto) {
        try {
            val offer = offerService.update(dto.offerId, State.COMPLETED, dto.valueTo!!)
            val offerDto = OfferDto.map(offer)
            kafkaProducer.sendMessage(ActionType.OFFER_COMPLETED, offerDto)
            log.debug("ExchangeOfferCompletedListenerHandler. Processed. exchangeRequestDto=$dto, offerDto=$offerDto")
        } catch (ex: Exception) {
            log.warn("ExchangeOfferCompletedListenerHandler. Failed. dto=$dto")
            dto.state = State.FAILED
            kafkaProducer.sendMessage(ActionType.OFFER_COMPLETED_FAILED, dto)
            throw ex
        }
    }
}
