package com.lesha.saga.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.lesha.saga.kafka.dto.CustomerDto
import com.lesha.saga.service.CustomerService
import org.springframework.stereotype.Component

//TODO MOCKED
@Component
class CustomerCreatedListenerHandler(
    private val om: ObjectMapper,
    private val customerService: CustomerService,
) : AbstractListenerHandler<CustomerDto>(om) {

    override val actionType: ActionType = ActionType.CUSTOMER_CREATED

    override fun mapEventToEntity(event: EventDto): CustomerDto {
        return om.readValue(event.payload, CustomerDto::class.java)
    }

    override fun handle(dto: CustomerDto) {
        val customer = dto.map()
        customerService.save(customer)
    }

}
