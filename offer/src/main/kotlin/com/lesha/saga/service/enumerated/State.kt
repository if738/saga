package com.lesha.saga.service.enumerated

enum class State {
    PENDING,
    CANCELED,
    COMPLETED,
    CANCEL_PENDING,
    FAILED,
}