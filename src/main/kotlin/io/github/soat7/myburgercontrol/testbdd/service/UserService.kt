package io.github.soat7.myburgercontrol.testbdd.service

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.soat7.myburgercontrol.testbdd.dto.UserCreationDTO
import io.github.soat7.myburgercontrol.testbdd.dto.UserRole
import io.github.soat7.myburgercontrol.testbdd.util.Configuration
import io.github.soat7.myburgercontrol.testbdd.util.DataSource
import io.restassured.builder.RequestSpecBuilder
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.Response
import java.util.UUID

private val log = KotlinLogging.logger { }

object UserService {
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

    fun isUserCreated(cpf: String): Boolean {
        log.info { "Check if user with CPF $cpf exists" }
        DataSource.connection().use { conn ->
            conn.prepareStatement("SELECT COUNT(*) FROM myburguer_user.user WHERE cpf = ?")
                .use { stmt ->
                    stmt.setString(1, cpf)
                    stmt.executeQuery().use { rs ->
                        if (rs.next()) {
                            return rs.getInt(1) > 0
                        } else {
                            return false
                        }
                    }
                }
        }
    }

    fun createUser(cpf: String, password: String, userRole: UserRole): Response = Given {
        spec(spec)
        contentType(ContentType.JSON)
    } When {
        body(config.objectMapper.writeValueAsString(UserCreationDTO(cpf, password, userRole)))
        post("/users")
    }

    fun findUserByCpf(cpf: String) = Given {
        spec(spec)
    } When {
        contentType(ContentType.JSON)
        queryParam("cpf", cpf)
        get("/users")
    }

    fun findUserByID(id: UUID): Response = Given {
        spec(spec)
        contentType(ContentType.JSON)
    } When {
        pathParam("id", id)
        get("/users/{id}")
    }

    fun deleteUsers(userIds: Collection<String>) {
        log.info { "Delete Users size: ${userIds.size}" }
        DataSource.connection().use { conn ->
            conn.prepareStatement("DELETE FROM myburguer_user.user WHERE id = ANY (?)")
                .use { stmt ->
                    stmt.setArray(1, conn.createArrayOf("uuid", userIds.toTypedArray()))
                    stmt.execute()
                }
        }
    }

    data class UserResponse(
        val id: UUID,
        val cpf: String,
        val role: UserRole,
    )
}
