package io.github.soat7.myburgercontrol.testbdd.dto

import java.math.BigDecimal
import java.util.Locale
import java.util.UUID

data class ProductDTO(
    val id: UUID? = null,
    val name: String,
    val description: String,
    val price: BigDecimal,
    val type: ProductType,
)

enum class ProductType {
    APPETIZER,
    DESSERT,
    DRINK,
    FOOD,
    OTHER,
    ;

    companion object {
        fun from(resource: String): ProductType {
            return try {
                ProductType.valueOf(resource.uppercase(Locale.getDefault()))
            } catch (ex: IllegalArgumentException) {
                throw ex
            }
        }
    }
}
