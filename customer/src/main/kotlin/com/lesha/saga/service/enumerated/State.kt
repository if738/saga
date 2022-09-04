package com.lesha.saga.service.enumerated

enum class State {
    PENDING,
    MONEY_RESERVED,
    CANCELED,
    COMPLETED,
    CANCEL_PENDING,
    FAILED,
}