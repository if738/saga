package com.lesha.saga.kafka.consumer

data class EventDto(
    var actionType: ActionType,
    var payload: String? = null
)
