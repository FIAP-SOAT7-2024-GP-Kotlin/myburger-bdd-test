package io.github.soat7.myburgercontrol.testbdd

import io.cucumber.java.ca.Quan
import io.cucumber.java.pt.Então
import io.github.oshai.kotlinlogging.KotlinLogging
import io.cucumber.java.pt.Quando
import io.github.soat7.myburgercontrol.testbdd.dto.OrderRequestPayment
import io.github.soat7.myburgercontrol.testbdd.dto.OrderRequestPaymentItem
import io.github.soat7.myburgercontrol.testbdd.dto.PaymentDTO
import io.github.soat7.myburgercontrol.testbdd.service.PaymentService
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Then
import io.restassured.response.Response
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers.equalTo
import org.postgresql.hostchooser.HostStatus
import java.math.BigDecimal
import java.util.UUID

private val log = KotlinLogging.logger { }

class PaymentServiceSteps {

    private lateinit var response: Response

    private val orderPaymentRequest = OrderRequestPayment(
        orderId = UUID.randomUUID(),
        totalAmount = BigDecimal.valueOf(100),
        items = mutableListOf(
            OrderRequestPaymentItem(
                description = "Descriçao hamburguer",
                totalAmount = BigDecimal.valueOf(50),
                title = "Hamburguer",
                quantity = 1,
                unitPrice = BigDecimal.valueOf(50),
                unitMeasure = "Unit",
            ),
            OrderRequestPaymentItem(
                description =  "Descriçao Soda",
                totalAmount = BigDecimal.valueOf(50),
                title = "Soda",
                quantity = 2,
                unitPrice = BigDecimal.valueOf(25),
                unitMeasure = "Unit",
            )
        )
    )

    @Quando("solicitado um pagamento de pedido")
    fun `solicitado um pagamento de pedido`(){
        response = PaymentService.create(orderPaymentRequest)
    }

    @Então("o sistema deve retornar as informações de solicitação com o status recebido")
    fun `o sistema deve retornar as informações de solicitação com o status recebido`(){
        response Then {
            statusCode(HttpStatus.SC_OK)

            body("status", equalTo("REQUESTED"))
        }
    }
}
