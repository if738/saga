package com.lesha.saga.service

import com.lesha.saga.enums.Operation
import com.lesha.saga.kafka.KafkaProducer
import com.lesha.saga.kafka.consumer.ActionType
import com.lesha.saga.kafka.dto.ExchangeRequestDto
import com.lesha.saga.listener.Asset
import com.lesha.saga.repository.entities.ExchangeRequest
import com.lesha.saga.service.enumerated.State
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class MakeDealService(
    private val exchangeRequestService: ExchangeRequestService,
    private val kafkaProducer: KafkaProducer,
) {

    fun deal(assets: List<Asset>) {
        val allWithMoneyReserved = exchangeRequestService.getAllWithMoneyReserved()
        calculateAndChangeStateIfTrue(allWithMoneyReserved, assets)
        val completedExchangeRequests = allWithMoneyReserved.filter { it.state == State.COMPLETED }
        exchangeRequestService.saveAll(completedExchangeRequests)
        completedExchangeRequests.forEach {
            val dto = ExchangeRequestDto.map(it)
            kafkaProducer.sendMessage(ActionType.EXCHANGE_OFFER_COMPLETED, dto)
        }
    }

    private fun calculateAndChangeStateIfTrue(
        allWithMoneyReserved: List<ExchangeRequest>,
        assets: List<Asset>
    ) {
        allWithMoneyReserved.forEach { er ->
            if (er.operation == Operation.SELL) {
                val sell = assets
                    .filter { it.operation == Operation.SELL }
                    .filter { er.currencyFrom == it.currencyFrom }
                    .filter { er.currencyTo == it.currencyTo }
                    .maxBy { it.rate }
                if (er.findBest) {
                    er.valueTo = er.valueFrom?.multiply(sell.rate)
                    er.state = State.COMPLETED
                }
                if (sell.rate.multiply(er.valueFrom) > er.valueTo && er.valueTo != BigDecimal.ZERO) {
                    er.state = State.COMPLETED
                }
                return@forEach
            }
            if (er.operation == Operation.BUY) {
                val buy = assets
                    .filter { it.operation == Operation.BUY }
                    .filter { er.currencyFrom == it.currencyFrom }
                    .filter { er.currencyTo == it.currencyTo }
                    .minBy { it.rate }
                if (er.findBest) {
                    er.valueTo = er.valueTo?.multiply(buy.rate)
                    er.state = State.COMPLETED
                }
                if (buy.rate.multiply(er.valueFrom) > er.valueTo && er.valueTo != BigDecimal.ZERO) {
                    er.state = State.COMPLETED
                }
                return@forEach
            }
        }
    }
}
