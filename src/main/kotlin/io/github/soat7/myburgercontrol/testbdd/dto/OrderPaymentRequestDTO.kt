package io.github.soat7.myburgercontrol.testbdd.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.math.BigDecimal
import java.util.UUID


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class OrderRequestPayment(
    val orderId: UUID,
    val items: List<OrderRequestPaymentItem>,
    val totalAmount: BigDecimal,
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class OrderRequestPaymentItem(
    val description: String,
    val title: String,
    val unitPrice: BigDecimal,
    val quantity: Int,
    val unitMeasure: String,
    val totalAmount: BigDecimal,
)
