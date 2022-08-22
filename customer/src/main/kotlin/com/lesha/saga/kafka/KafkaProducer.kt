package com.lesha.saga.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.lesha.saga.kafka.consumer.ActionType
import com.lesha.saga.kafka.consumer.EventDto
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.Header
import org.apache.kafka.common.header.internals.RecordHeader
import org.apache.kafka.common.utils.Bytes
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaProducer(
    private val kafkaTemplate: KafkaTemplate<String, Any>,
    @Value("\${spring.kafka.producer.topic}")
    private val notificationTopic: String, private val om: ObjectMapper
) {

    fun sendMessage(actionType: ActionType, dto: Any) {
        val payload = om.writeValueAsString(dto)
        val event = EventDto(actionType, payload)
        val recordHeader = RecordHeader("actionType", actionType.name.toByteArray())
        val producerRecord = ProducerRecord<String, Any>(
            notificationTopic, 0, null, null, payload, listOf(recordHeader)
        )
        kafkaTemplate.usingCompletableFuture().send(producerRecord)
    }

}