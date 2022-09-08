//package com.lesha.saga.generator
//
//import com.lesha.saga.repository.CustomerRepository
//import com.lesha.saga.repository.entities.enums.Currency
//import com.lesha.saga.service.WalletService
//import org.springframework.scheduling.annotation.Scheduled
//import org.springframework.stereotype.Component
//import java.math.BigDecimal
//import kotlin.random.Random
//
//@Component
//class GenerateTopUp(
//    private val customerRepository: CustomerRepository,
//    private val walletService: WalletService,
//) {
//
//    @Scheduled(fixedDelay = 100L)
//    fun generate() {
//        val customers = customerRepository.findAll().toList()
//        customers.forEach { customer ->
//            walletService.topUp(BigDecimal(Random.nextDouble(0.0000495, 0.0000505)), Currency.BTC, customer.id)
//            walletService.topUp(BigDecimal(Random.nextDouble(20000.0, 20200.0)), Currency.USD, customer.id)
//        }
//    }
//
//}