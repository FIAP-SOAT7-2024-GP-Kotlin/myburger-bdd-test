package io.github.soat7.myburgercontrol.testbdd.service

import io.github.soat7.myburgercontrol.testbdd.dto.OrderCreationDTO
import io.github.soat7.myburgercontrol.testbdd.dto.OrderDTO
import io.github.soat7.myburgercontrol.testbdd.dto.OrderRequestPayment
import io.github.soat7.myburgercontrol.testbdd.service.PaymentService.spec
import io.github.soat7.myburgercontrol.testbdd.util.Configuration
import io.restassured.builder.RequestSpecBuilder
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.When

object PaymentService {
    private val config = Configuration
    private val spec = RequestSpecBuilder()
        .setBaseUri(config["myburger.baseUri"])
        .setContentType(ContentType.JSON)
        .build()

    fun create(orderPaymentRequest: OrderRequestPayment) = Given {
        spec(spec)
        contentType(ContentType.JSON)
    } When {
        body(config.objectMapper.writeValueAsString(orderPaymentRequest))
        post("/orders")
    }
}
