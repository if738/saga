package com.lesha.saga

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
@EnableJpaRepositories("com.lesha.saga.repository")
class OfferApplication

fun main(args: Array<String>) {
    runApplication<OfferApplication>(*args)
}
