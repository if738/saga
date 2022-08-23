package com.lesha.saga.kafka.consumer

interface ListenerHandler<E> {
    val actionType: ActionType
    fun apply(event: EventDto): String
    fun handle(entity: E)
    fun mapEventToEntity(event: EventDto): E
}
