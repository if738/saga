package com.lesha.saga.service.enumerated

enum class State {
    PENDING,
    MONEY_RESERVED,
    WAITING_FOR_RESERVE,
    CANCELED,
    COMPLETED,
    CANCEL_PENDING,
    FAILED,
}