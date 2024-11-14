package io.github.soat7.myburgercontrol.testbdd.service

import io.github.soat7.myburgercontrol.testbdd.util.Configuration
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.http.ContentType
import io.restassured.response.Response

object AuthService {
    private val config = Configuration
    private val spec = RequestSpecBuilder()
        .setBaseUri(config["myburger.baseUri"])
        .setContentType(ContentType.JSON)
        .build()

    fun loginAccessToken(cpf: String, password: String): String = loginCall(cpf, password)
        .then()
        .statusCode(200)
        .log().all() // Log the response for debugging purposes
        .extract()
        .path("access_token")

    fun loginCall(cpf: String, password: String): Response = given()
        .spec(spec)
        .body(
            config.objectMapper.writeValueAsString(
                mapOf(
                    "cpf" to cpf,
                    "password" to password,
                ),
            ),
        )
        .post("/auth")
}
