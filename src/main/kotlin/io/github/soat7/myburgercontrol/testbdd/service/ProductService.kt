package io.github.soat7.myburgercontrol.testbdd.service

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.soat7.myburgercontrol.testbdd.dto.ProductDTO
import io.github.soat7.myburgercontrol.testbdd.dto.ProductType
import io.github.soat7.myburgercontrol.testbdd.util.Configuration
import io.github.soat7.myburgercontrol.testbdd.util.DataSource
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.http.ContentType

private val log = KotlinLogging.logger { }

object ProductService {

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

    fun createProduct(productDTO: ProductDTO) = given(spec)
        .contentType(ContentType.JSON)
        .`when`()
        .body(config.objectMapper.writeValueAsString(productDTO))
        .post("/products")

    fun getProductById(productId: String) = given(spec)
        .contentType(ContentType.JSON)
        .`when`()
        .get("/products/$productId")


    fun getProducts() = given(spec)
        .contentType(ContentType.JSON)
        .`when`()
        .queryParam("page", 0)
        .queryParam("size", 20)
        .get("/products")

    fun getProductByType(type: ProductType) = given(spec)
        .contentType(ContentType.JSON)
        .`when`()
        .queryParam("type", type.name)
        .get("/products/type")


    fun deleteProducts(productIds: Collection<String>) {
        log.info { "Delete Products size: ${productIds.size}" }
        DataSource.connection().use { conn ->
            conn.prepareStatement("DELETE FROM myburguer.product WHERE id = ANY (?)").use { stmt ->
                stmt.setArray(1, conn.createArrayOf("uuid", productIds.toTypedArray()))
                stmt.executeUpdate()
            }
        }
    }
}
