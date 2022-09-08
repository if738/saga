package com.lesha.saga.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.lesha.saga.kafka.KafkaProducer
import com.lesha.saga.kafka.dto.OfferDto
import com.lesha.saga.service.ReservedBalanceService
import com.lesha.saga.service.enumerated.State
import org.hibernate.annotations.common.util.impl.LoggerFactory
import org.springframework.stereotype.Component

@Component
class OfferRollbackListenerHandler(
    private val om: ObjectMapper,
    private val reservedBalanceService: ReservedBalanceService,
    private val kafkaProducer: KafkaProducer,
) : AbstractListenerHandler<OfferDto>(om) {

    override val actionType: ActionType = ActionType.OFFER_ROLLBACK
    private val log = LoggerFactory.logger(this::class.java)

    override fun mapEventToEntity(event: EventDto): OfferDto {
        return om.readValue(event.payload, OfferDto::class.java)
    }

    override fun handle(dto: OfferDto) {
        try {
            reservedBalanceService.handleCancellation(dto.id)
            log.debug("OfferRollbackListenerHandler. Processed. dto=$dto")
        } catch (ex: Exception) {
            log.warn("OfferRollbackListenerHandler. Failed. dto=$dto")
            dto.state = State.FAILED
            kafkaProducer.sendMessage(ActionType.CUSTOMER_BALANCE_RESERVE_ROLLBACK_FAILED, dto)
            throw ex
        }
    }
}
