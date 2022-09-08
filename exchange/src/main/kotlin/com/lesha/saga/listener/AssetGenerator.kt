package com.lesha.saga.listener

import com.lesha.saga.enums.Operation
import com.lesha.saga.repository.entities.enums.Currency
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.random.Random

object AssetGenerator {

    fun generateBatch(count: Int): List<Asset> {
        val  list: MutableList<Asset> = mutableListOf()
        repeat(count){
            val usdBtc = BigDecimal(0.00005)
            val btcUsd = BigDecimal(20000)
            val usdBtcSell = usdBtc.multiply(BigDecimal(Random.nextDouble(0.98, 0.99))).setScale(17, RoundingMode.HALF_UP)
            val btcUsdSell = btcUsd.multiply(BigDecimal(Random.nextDouble(0.98, 0.99))).setScale(17, RoundingMode.HALF_UP)
            val usdBtcBuy = usdBtc.multiply(BigDecimal(Random.nextDouble(1.01, 1.02))).setScale(17, RoundingMode.HALF_UP)
            val btcUsdBuy = btcUsd.multiply(BigDecimal(Random.nextDouble(1.01, 1.02))).setScale(17, RoundingMode.HALF_UP)
            list.add(Asset(Operation.SELL, Currency.USD, Currency.BTC, usdBtcSell))
            list.add(Asset(Operation.SELL, Currency.BTC, Currency.USD, btcUsdSell))
            list.add(Asset(Operation.BUY, Currency.USD, Currency.BTC, usdBtcBuy))
            list.add(Asset(Operation.BUY, Currency.BTC, Currency.USD, btcUsdBuy))
        }
        return list
    }

}