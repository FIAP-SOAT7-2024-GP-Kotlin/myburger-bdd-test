package io.github.soat7.myburgercontrol.testbdd.service

import io.github.soat7.myburgercontrol.testbdd.util.Configuration
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.http.ContentType

object AuthService {
    private val config = Configuration
    private val spec = RequestSpecBuilder()
        .setBaseUri(config["myburger.baseUri"])
        .setContentType(ContentType.JSON)
        .build()

    fun login(cpf: String, password: String) = given()
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
        .then()
        .statusCode(200)
        .extract()
        .path<String>("accessToken")
}
