package com.lesha.saga.service.enumerated

enum class State(val priority: Short) {
    PENDING(1),
    MONEY_RESERVED(2),
    CANCELED(3),
    COMPLETED(3),
    CANCEL_PENDING(3),
    FAILED(3),
}