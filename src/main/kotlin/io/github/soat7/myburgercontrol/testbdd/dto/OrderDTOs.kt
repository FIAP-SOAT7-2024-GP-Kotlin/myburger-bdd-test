package io.github.soat7.myburgercontrol.testbdd.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.github.oshai.kotlinlogging.KotlinLogging
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

private val log = KotlinLogging.logger { }

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class OrderCreationDTO(
    val customerCpf: String,
    val items: List<OrderItemCreationDTO>,
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class OrderItemCreationDTO(
    val productId: UUID,
    val quantity: Int,
    val price: BigDecimal,
    val comment: String? = null,
)

data class OrderDTO(
    val id: UUID,
    val customer: CustomerDTO?,
    val items: MutableList<OrderItemDTO> = mutableListOf(),
    val status: OrderStatus,
    val createdAt: Instant,
    val total: BigDecimal,
)

data class OrderItemDTO(
    val productId: UUID?,
    val price: BigDecimal,
    val quantity: Int,
    val comment: String? = null
)


enum class OrderStatus {
    NEW,
    RECEIVED,
    IN_PROGRESS,
    READY,
    FINISHED,
    ;

    companion object {

        fun from(source: String): OrderStatus = try {
            OrderStatus.valueOf(source)
        } catch (ex: IllegalArgumentException) {
            log.error(ex) { ex.message }
            throw ex
        }
    }
}
