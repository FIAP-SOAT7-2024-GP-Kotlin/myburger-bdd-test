package io.github.soat7.myburgercontrol.testbdd.service

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.soat7.myburgercontrol.testbdd.dto.CustomerDTO
import io.github.soat7.myburgercontrol.testbdd.util.Configuration
import io.github.soat7.myburgercontrol.testbdd.util.DataSource
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.http.ContentType

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

    fun createCustomer(inputCustomer: CustomerDTO) = given(spec)
        .contentType(ContentType.JSON)
        .`when`()
        .body(config.objectMapper.writeValueAsString(inputCustomer))
        .post("/customers")

    fun getCustomerById(id: String) = given(spec)
        .contentType(ContentType.JSON)
        .`when`()
        .get("/customers/$id")

    fun getCustomerByCPF(cpf: String) = given(spec)
        .contentType(ContentType.JSON)
        .`when`()
        .queryParam("cpf", cpf)
        .get("/customers")

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
