package io.github.soat7.myburgercontrol.testbdd.service

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.soat7.myburgercontrol.testbdd.dto.CustomerDTO
import io.github.soat7.myburgercontrol.testbdd.util.Configuration
import io.github.soat7.myburgercontrol.testbdd.util.DataSource
import io.restassured.builder.RequestSpecBuilder
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.When

private val log = KotlinLogging.logger { }

object CustomerService {

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

    fun createCustomer(inputCustomer: CustomerDTO) = Given {
        spec(spec)
        contentType(ContentType.JSON)
    } When {
        body(config.objectMapper.writeValueAsString(inputCustomer))
        post("/customers")
    }

    fun getCustomerById(id: String) = Given {
        spec(spec)
        contentType(ContentType.JSON)
    } When {
        get("/customers/$id")
    }

    fun getCustomerByCPF(cpf: String) = Given {
        spec(spec)
        contentType(ContentType.JSON)
    } When {
        queryParam("cpf", cpf)
        get("/customers")
    }

    fun deleteCustomers(customerIds: Collection<String>) {
        log.info { "Delete Products size: ${customerIds.size}" }
        DataSource.connection().use { conn ->
            conn.prepareStatement("DELETE FROM myburguer.customer WHERE id = ANY (?)").use { stmt ->
                stmt.setArray(1, conn.createArrayOf("uuid", customerIds.toTypedArray()))
                stmt.executeUpdate()
            }
        }
    }
}
