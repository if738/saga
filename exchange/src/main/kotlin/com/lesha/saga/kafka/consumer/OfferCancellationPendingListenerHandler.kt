package com.lesha.saga.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.lesha.saga.kafka.KafkaProducer
import com.lesha.saga.kafka.dto.OfferDto
import com.lesha.saga.service.ExchangeRequestService
import com.lesha.saga.service.enumerated.State
import org.hibernate.annotations.common.util.impl.LoggerFactory
import org.springframework.stereotype.Component

@Component
class OfferCancellationPendingListenerHandler(
    private val om: ObjectMapper,
    private val exchangeRequestService: ExchangeRequestService,
    private val kafkaProducer: KafkaProducer,
) : AbstractListenerHandler<OfferDto>(om) {

    override val actionType: ActionType = ActionType.OFFER_CANCELLATION_PENDING
    private val log = LoggerFactory.logger(this::class.java)

    override fun mapEventToEntity(event: EventDto): OfferDto {
        return om.readValue(event.payload, OfferDto::class.java)
    }

    override fun handle(dto: OfferDto) {
        try {
            val offer = dto.map()
            val exchangeRequest = exchangeRequestService.process(offer, State.CANCELED)
            kafkaProducer.sendMessage(ActionType.EXCHANGE_OFFER_CANCELED, exchangeRequest)
            log.debug("OfferCancellationPendingListenerHandler. processed dto=$dto")
        } catch (ex: Exception) {
            log.warn("OfferCancellationPendingListenerHandler. Failed. dto=$dto")
            dto.state = State.FAILED
            kafkaProducer.sendMessage(ActionType.EXCHANGE_OFFER_CANCELLATION_FAILED, dto)
            throw ex
        }
    }
}
