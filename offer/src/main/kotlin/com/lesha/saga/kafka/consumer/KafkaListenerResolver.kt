package com.lesha.saga.kafka.consumer

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.hibernate.annotations.common.util.impl.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class KafkaListenerResolver(
    private val listenerHandler: List<ListenerHandler<*>>,
) {

    private val log = LoggerFactory.logger(this::class.java)

    @KafkaListener(topics = ["topic"], groupId = "\${spring.kafka.event.group_id}")
    fun listen(record: ConsumerRecord<String, Any>) {
        val payload = record.value()
        val actionType = getActionType(record) ?: return
        var handlers: List<ListenerHandler<*>>? = null
        try {
            handlers = listenerHandler.filter { it.actionType == actionType }
        } catch (e: Exception) {
            log.trace("Action Type Not supported $actionType")
        }
        handlers?.forEach {
            it.apply(EventDto(actionType, payload as String?))
        }
        log.trace("Record received value=${record}")
    }

    private fun getActionType(record: ConsumerRecord<String, Any>): ActionType? {
        val headers = record.headers().toMutableList()
        val actionTypeByteArray = headers.find { it.key() == "actionType" }?.value() ?: return null
        var actionTypeString: String? = null
        return try {
            actionTypeString = String(actionTypeByteArray)
            ActionType.valueOf(actionTypeString)
        } catch (ex: Exception) {
            log.warn("KafkaListenerResolver. actionType=$actionTypeString not found. supported ${ActionType.values()}")
            null
        }
    }

}
