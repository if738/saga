package com.lesha.saga

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories("com.lesha.saga.repository")
class CustomerApplication

fun main(args: Array<String>) {
    runApplication<CustomerApplication>(*args)
}