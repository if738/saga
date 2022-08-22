package com.lesha.saga.configuration

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
class KafkaTopicConfig(@Value(value = "\${spring.kafka.producer.bootstrap-servers}") private val bootstrapAddress: String) {

    @Bean
    fun producerConfigServer() = hashMapOf<String, Any>(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapAddress,
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java
    )

    @Bean
    fun producerFactoryServer() = DefaultKafkaProducerFactory<String, Any>(producerConfigServer())

    @Bean
    fun kafkaTemplate() = KafkaTemplate(producerFactoryServer())

}