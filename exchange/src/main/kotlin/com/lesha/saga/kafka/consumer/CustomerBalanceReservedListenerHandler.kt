package com.lesha.saga.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.lesha.saga.kafka.KafkaProducer
import com.lesha.saga.kafka.dto.OfferDto
import com.lesha.saga.service.ExchangeRequestService
import com.lesha.saga.service.enumerated.State
import org.hibernate.annotations.common.util.impl.LoggerFactory
import org.springframework.stereotype.Component
import java.math.BigDecimal
import kotlin.random.Random

@Component
class CustomerBalanceReservedListenerHandler(
    private val om: ObjectMapper,
    private val exchangeRequestService: ExchangeRequestService,
    private val kafkaProducer: KafkaProducer,
) : AbstractListenerHandler<OfferDto>(om) {

    override val actionType: ActionType = ActionType.CUSTOMER_BALANCE_RESERVED
    private val log = LoggerFactory.logger(this::class.java)

    override fun mapEventToEntity(event: EventDto): OfferDto {
        return om.readValue(event.payload, OfferDto::class.java)
    }

    override fun handle(dto: OfferDto) {
        //throw error with 1/30 chance
        try {
//            if (Random.nextInt(30) == 1) throw AssertionError("ExpectedException! 1/30 change!")
            if (dto.valueFrom == BigDecimal.ZERO) throw RuntimeException("valueFrom can't be null")
            if (dto.state != State.MONEY_RESERVED) throw RuntimeException("Can process only State.MONEY_RESERVED")
            val offer = dto.map()
            val exchangeRequest = exchangeRequestService.process(offer, State.MONEY_RESERVED)
            kafkaProducer.sendMessage(ActionType.EXCHANGE_OFFER_RESERVED, exchangeRequest)
            log.debug("CustomerBalanceReservedListenerHandler. Processed dto=$dto")
        } catch (aEx: AssertionError) {
            log.debug("ExpectedException! 1/30 change!")
        } catch (ex: RuntimeException) {
            log.warn("CustomerBalanceReservedListenerHandler. Failed dto=$dto")
            dto.state = State.FAILED
            kafkaProducer.sendMessage(ActionType.EXCHANGE_OFFER_RESERVATION_FAILED, dto)
            throw ex
        }
    }
}
