package io.github.soat7.myburgercontrol.testbdd.service

import io.github.soat7.myburgercontrol.testbdd.util.Configuration
import io.restassured.builder.RequestSpecBuilder
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.Response

object AuthService {
    private val config = Configuration
    private val spec = RequestSpecBuilder()
        .setBaseUri(config["myburger.baseUri"])
        .setContentType(ContentType.JSON)
        .build()

    fun loginAccessToken(cpf: String, password: String): String = loginCall(cpf, password) Then {
        statusCode(202)
        log().all() // Log the response for debugging purposes
    } Extract {
        path("access_token")
    }

    fun loginCall(cpf: String, password: String): Response = Given {
        spec(spec)
    } When {
        body(
            config.objectMapper.writeValueAsString(
                mapOf(
                    "cpf" to cpf,
                    "password" to password,
                ),
            ),
        )
        post("/auth")
    }
}
