package com.lesha.saga.service.entity

enum class State {
    PENDING,
    CANCELED,
    COMPLETED,
    CANCEL_PENDING,
    FAILED,
}