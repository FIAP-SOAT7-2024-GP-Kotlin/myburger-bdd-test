package io.github.soat7.myburgercontrol.testbdd.service

import io.github.soat7.myburgercontrol.testbdd.dto.UserCreationDTO
import io.github.soat7.myburgercontrol.testbdd.dto.UserRole
import io.github.soat7.myburgercontrol.testbdd.util.Configuration
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.http.ContentType
import org.apache.http.HttpStatus
import java.util.UUID

object UserService {
    private val config = Configuration
    private val spec = RequestSpecBuilder()
        .setBaseUri(config["myburger.baseUri"])
        .build()

    fun createUser(cpf: String, password: String, userRole: UserRole) = given().spec(spec)
        .`when`()
        .contentType(ContentType.JSON)
        .body(config.objectMapper.writeValueAsString(UserCreationDTO(cpf, password, userRole)))
        .post("/users")


    fun findUserByCpf(cpf: String) = given().spec(spec)
        .`when`()
        .contentType(ContentType.JSON)
        .queryParam("cpf", cpf)
        .get("/users")
        .then()
        .statusCode(HttpStatus.SC_OK)
        .extract()
        .`as`(UserResponse::class.java)

    data class UserResponse(
        val id: UUID,
        val cpf: String,
        val role: UserRole,
    )
}
