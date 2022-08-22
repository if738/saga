package com.lesha.saga.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.hibernate.annotations.common.util.impl.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class KafkaListenerResolver(
    private val listenerHandler: List<ListenerHandler<*>>,
    private val om: ObjectMapper,
) {

    private val log = LoggerFactory.logger(this::class.java)

    @KafkaListener(topics = ["topic"], groupId = "\${spring.kafka.event.group_id}")
    fun listen(record: ConsumerRecord<String, Any>) {
        val payload = record.value()
        val actionType = getActionType(record)
        try {
            listenerHandler.filter { it.actionType == actionType }
                .forEach {
                    it.apply(EventDto(actionType, payload as String?))
                }
        } catch (e: Exception) {
            log.trace("Action Type Not supported $actionType")
        }
        log.trace("Record received value=${record}")
    }

    private fun getActionType(record: ConsumerRecord<String, Any>): ActionType {
        val headers = record.headers().toMutableList()
        val actionTypeByteArray = headers.find { it.key() == "actionType" }?.value()
        return ActionType.valueOf(String(actionTypeByteArray as ByteArray))
    }

}
