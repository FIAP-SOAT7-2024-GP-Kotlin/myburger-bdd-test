package io.github.soat7.myburgercontrol.testbdd.service

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.soat7.myburgercontrol.testbdd.dto.ProductDTO
import io.github.soat7.myburgercontrol.testbdd.dto.ProductType
import io.github.soat7.myburgercontrol.testbdd.util.Configuration
import io.github.soat7.myburgercontrol.testbdd.util.DataSource
import io.restassured.builder.RequestSpecBuilder
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.When

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

    fun createProduct(productDTO: ProductDTO) = Given {
        spec
        contentType(ContentType.JSON)
    } When {
        body(config.objectMapper.writeValueAsString(productDTO))
        post("/products")
    }

    fun getProductById(productId: String) = Given {
        spec
        contentType(ContentType.JSON)
    } When {
        get("/products/$productId")
    }

    fun getProducts() = Given {
        spec(spec)
        contentType(ContentType.JSON)
    } When {
        queryParam("page", 0)
        queryParam("size", 20)
        get("/products")
    }

    fun getProductByType(type: ProductType) = Given {
        spec(spec)
        contentType(ContentType.JSON)
    } When {
        queryParam("type", type.name)
        get("/products/type")
    }

    fun deleteProduct(productId: String) = Given {
        spec(spec)
        contentType(ContentType.JSON)
    } When {
        delete("/products/$productId")
    }

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
