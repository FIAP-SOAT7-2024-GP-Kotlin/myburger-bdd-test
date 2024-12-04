package io.github.soat7.myburgercontrol.testbdd.dto

data class PaymentDTO (
        val id: String,
        val status: String,
        val orderId: String,
        val orderPrice: String,
        val qrcode: String
)
