package com.lesha.saga.kafka.consumer

enum class ActionType {
    CUSTOMER_CREATED,

    CUSTOMER_BALANCE_RESERVED,
    CUSTOMER_BALANCE_RESERVE_FAILED,
    CUSTOMER_BALANCE_RESERVE_CANCELED,
    CUSTOMER_BALANCE_RESERVE_CANCELED_FAILED,
    CUSTOMER_BALANCE_RESERVE_ROLLBACK_FAILED,
    OFFER_CREATED,
    OFFER_CREATED_FAILED,
    OFFER_CANCELED,
    OFFER_CANCELED_FAILED,
    OFFER_COMPLETED,

    EXCHANGE_OFFER_CANCELED,
    EXCHANGE_OFFER_COMPLETED,
    EXCHANGE_OFFER_FAILED,
    OFFER_ROLLBACK,
}
