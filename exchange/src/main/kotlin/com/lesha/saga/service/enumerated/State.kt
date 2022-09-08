package com.lesha.saga.service.enumerated

enum class State(val priority: Short) {
    MONEY_RESERVED(2),
    COMPLETED(3),
    CANCELED(3),
    CANCEL_PENDING(3),
    FAILED(3);
}