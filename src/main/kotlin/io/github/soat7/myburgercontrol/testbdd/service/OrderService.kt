package io.github.soat7.myburgercontrol.testbdd.service

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.soat7.myburgercontrol.testbdd.dto.OrderCreationDTO
import io.github.soat7.myburgercontrol.testbdd.util.Configuration
import io.restassured.builder.RequestSpecBuilder
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.When

private val log = KotlinLogging.logger { }

object OrderService {
    private val config = Configuration
    private var spec = RequestSpecBuilder()
        .setBaseUri(config["myburger.baseUri"])
        .build()

    fun updateAccessToken(accessToken: String?) {
        spec = RequestSpecBuilder()
            .setBaseUri(config["myburger.baseUri"]).let {
                if (accessToken != null) {
                    it.addHeader("Authorization", "Bearer $accessToken")
                } else {
                    it
                }
            }
            .build()
    }

    fun create(order: OrderCreationDTO) = Given {
        spec(spec)
        contentType(ContentType.JSON)
    } When {
        body(config.objectMapper.writeValueAsString(order))
        post("/orders")
    }
}
