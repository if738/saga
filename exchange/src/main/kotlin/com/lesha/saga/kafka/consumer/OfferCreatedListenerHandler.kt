package com.lesha.saga.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.lesha.saga.kafka.KafkaProducer
import com.lesha.saga.kafka.dto.OfferDto
import com.lesha.saga.service.ExchangeRequestService
import com.lesha.saga.service.enumerated.State
import org.hibernate.annotations.common.util.impl.LoggerFactory
import org.springframework.stereotype.Component
import java.math.BigDecimal

//TODO FOR TESTING
@Component
class OfferCreatedListenerHandler(
    private val om: ObjectMapper,
    private val exchangeRequestService: ExchangeRequestService,
    private val kafkaProducer: KafkaProducer,
) : AbstractListenerHandler<OfferDto>(om) {

    override val actionType: ActionType = ActionType.OFFER_CREATED
    private val log = LoggerFactory.logger(this::class.java)

    override fun mapEventToEntity(event: EventDto): OfferDto {
        return om.readValue(event.payload, OfferDto::class.java)
    }

    override fun handle(dto: OfferDto) {
        if (dto.valueFrom == BigDecimal.ZERO) throw RuntimeException("valueFrom can't be null")
        try {
            val offer = dto.map()
            exchangeRequestService.process(offer)
            log.debug("OfferCreatedListenerHandler. processed=$dto")
        } catch (ex: Exception) {
            log.warn("ReservationFailed. dto=$dto")
            dto.state = State.FAILED
            kafkaProducer.sendMessage(ActionType.CUSTOMER_BALANCE_RESERVE_FAILED, dto)
        }
    }
}
