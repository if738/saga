package com.lesha.saga.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper

abstract class AbstractListenerHandler<E>(
    private val om: ObjectMapper
): ListenerHandler<E> {

    abstract override val actionType: ActionType

    override fun apply(event: EventDto): String {
        val result = mapEventToEntity(event)
        handle(result)
        return om.writeValueAsString(result)
    }

    abstract override fun handle(dto: E)

    abstract override fun mapEventToEntity(event: EventDto): E

}