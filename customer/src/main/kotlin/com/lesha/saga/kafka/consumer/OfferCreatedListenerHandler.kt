package com.lesha.saga.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.lesha.saga.kafka.KafkaProducer
import com.lesha.saga.kafka.dto.OfferDto
import com.lesha.saga.service.ReservedBalanceService
import com.lesha.saga.service.enumerated.State
import org.hibernate.annotations.common.util.impl.LoggerFactory
import org.springframework.kafka.KafkaException
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class OfferCreatedListenerHandler(
    private val om: ObjectMapper,
    private val reservedBalanceService: ReservedBalanceService,
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
            reservedBalanceService.reserve(dto.customerId, dto.id, dto.valueFrom, dto.currencyFrom)
            val copyOfferDto = dto.copy()
            copyOfferDto.state = State.MONEY_RESERVED
            kafkaProducer.sendMessage(ActionType.CUSTOMER_BALANCE_RESERVED, copyOfferDto)
        } catch (aEx: ArithmeticException) {
            log.trace("Not enought money")
        }
        catch (ex: Exception) {
            log.warn("OfferCreatedListenerHandler. ReservationFailed. dto=$dto exception=$ex")
            dto.state = State.FAILED
            kafkaProducer.sendMessage(ActionType.CUSTOMER_BALANCE_RESERVE_FAILED, dto)
            throw ex
        }
    }
}
