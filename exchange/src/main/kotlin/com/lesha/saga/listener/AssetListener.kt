package com.lesha.saga.listener

import com.lesha.saga.service.MakeDealService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class AssetListener(private val makeDealService: MakeDealService) {

    @Scheduled(fixedDelay = 1000)
    fun generateAssets() {
        val generateBatch = AssetGenerator.generateBatch(100)
        makeDealService.deal(generateBatch)
    }
}
