package io.github.soat7.myburgercontrol.testbdd

import io.cucumber.java.After
import io.cucumber.java.pt.Dado
import io.cucumber.java.pt.E
import io.cucumber.java.pt.Entao
import io.cucumber.java.pt.Quando
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.serpro69.kfaker.faker
import io.github.soat7.myburgercontrol.testbdd.dto.OrderCreationDTO
import io.github.soat7.myburgercontrol.testbdd.dto.OrderItemCreationDTO
import io.github.soat7.myburgercontrol.testbdd.dto.ProductDTO
import io.github.soat7.myburgercontrol.testbdd.dto.ProductType
import io.github.soat7.myburgercontrol.testbdd.dto.UserRole
import io.github.soat7.myburgercontrol.testbdd.service.AuthService
import io.github.soat7.myburgercontrol.testbdd.service.OrderService
import io.github.soat7.myburgercontrol.testbdd.service.ProductService
import io.github.soat7.myburgercontrol.testbdd.service.UserService
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Then
import io.restassured.response.Response
import org.apache.http.HttpStatus

private val log = KotlinLogging.logger { }

class OrderServiceSteps {
    private lateinit var response: Response
    private val cpf = "11111111111"
    private val password = "123"
    private lateinit var accessToken: String
    private lateinit var createdUser: UserService.UserResponse

    companion object {
        private val createdUserIds = mutableListOf<String>()
    }

    private val faker = faker { this.fakerConfig { locale = "pt-BR" } }

    @Dado("que o usuário está logado no sistema")
    fun `que o usuario esta logado no sistema`() {
        if (!UserService.isUserCreated(cpf)) {
            UserService.updateAccessToken(null)
            createdUser = UserService.createUser(
                cpf = cpf,
                password = password,
                userRole = UserRole.USER,
            ) Then {
                statusCode(HttpStatus.SC_OK)
            } Extract {
                path("content")
            }
            createdUserIds.add(createdUser.id.toString())
        }
        accessToken = AuthService.loginAccessToken(cpf, password)
        ProductService.updateAccessToken(accessToken)
        OrderService.updateAccessToken(accessToken)
    }

    private fun getOneProductWithType(type: ProductType) = ProductService.getProductByType(type) Then {
        statusCode(HttpStatus.SC_OK)
    } Extract {
        path<List<ProductDTO>>("content").first()
    }

    @Quando("o usuário fecha um pedido com produtos adicionados")
    fun `o usuario fecha um pedido com produtos adicionados`() {
        val food = getOneProductWithType(ProductType.FOOD)
        val drink = getOneProductWithType(ProductType.DRINK)

        val order = OrderCreationDTO(
            customerCpf = createdUser.cpf,
            items = listOf(
                OrderItemCreationDTO(
                    productId = food.id!!,
                    price = food.price,
                    quantity = 1,
                ),
                OrderItemCreationDTO(
                    productId = drink.id!!,
                    price = drink.price,
                    quantity = 1,
                ),
            ),
        )

        response = OrderService.create(order = order)
    }

    @Entao("o status do pedido deve ser \"Novo\"")
    fun `o status do pedido deve ser Novo`() {
        response Then {
            log().all()
            statusCode(HttpStatus.SC_OK)
        }
    }

    @E("o pedido deve ser criado e os produtos devem estar associados a ele")
    fun `o pedido deve ser criado e os produtos devem estar associados a ele`() {
        throw NotImplementedError()
    }

    @Dado("que existe algum pedido associado ao CPF do usuário")
    fun `que existe algum pedido associado ao CPF do usuario`() {
        throw NotImplementedError()
    }

    @Quando("o usuário solicita a lista de pedidos para o seu CPF")
    fun `o usuario solicita a lista de pedidos para o seu CPF`() {
        throw NotImplementedError()
    }

    @Entao("a lista de pedidos deve ser exibida")
    fun `a lista de pedidos deve ser exibida`() {
        throw NotImplementedError()
    }

    @Dado("que existem pedidos no sistema")
    fun `que existem pedidos no sistema`() {
        throw NotImplementedError()
    }

    @Quando("o usuário solicita a lista de todos os pedidos")
    fun `o usuario solicita a lista de todos os pedidos`() {
        throw NotImplementedError()
    }

    @Entao("a lista de todos os pedidos deve ser exibida")
    fun `a lista de todos os pedidos deve ser exibida`() {
        throw NotImplementedError()
    }

    @Dado("que existem pedidos no sistema com status \"Novo\"")
    fun `que existem pedidos no sistema com status Novo`() {
        throw NotImplementedError()
    }

    @Quando("o usuário solicita a lista da fila de pedidos")
    fun `o usuario solicita a lista da fila de pedidos`() {
        throw NotImplementedError()
    }

    @Entao("a lista da fila de pedidos deve ser exibida")
    fun `a lista da fila de pedidos deve ser exibida`() {
        throw NotImplementedError()
    }

    @E("o status dos pedidos na fila deve ser \"Novo\"")
    fun `o status dos pedidos na fila deve ser Novo`() {
        throw NotImplementedError()
    }

    @Dado("que existe um pedido com status \"Novo\"")
    fun `que existe um pedido com status Novo`() {
        throw NotImplementedError()
    }

    @Quando("o usuário coloca o pedido em preparação")
    fun `o usuario coloca o pedido em preparacao`() {
        throw NotImplementedError()
    }

    @Entao("o status do pedido deve ser alterado para \"Em Preparação\"")
    fun `o status do pedido deve ser alterado para Em Preparacao`() {
        throw NotImplementedError()
    }

    @Dado("que existe um pedido com status \"Em Preparação\"")
    fun `que existe um pedido com status Em Preparacao`() {
        throw NotImplementedError()
    }

    @Quando("o usuário termina o preparo do pedido")
    fun `o usuario termina o preparo do pedido`() {
        throw NotImplementedError()
    }

    @Entao("o status do pedido deve ser alterado para \"Pronto\"")
    fun `o status do pedido deve ser alterado para Pronto`() {
        throw NotImplementedError()
    }

    @Dado("que existe um pedido com status \"Pronto\"")
    fun `que existe um pedido com status Pronto`() {
        throw NotImplementedError()
    }

    @Quando("o usuário marca o pedido como recebido")
    fun `o usuario marca o pedido como recebido`() {
        throw NotImplementedError()
    }

    @Entao("o status do pedido deve ser alterado para \"Recebido\"")
    fun `o status do pedido deve ser alterado para Recebido`() {
        throw NotImplementedError()
    }

    @Dado("que existe um pedido com status \"Recebido\"")
    fun `que existe um pedido com status Recebido`() {
        throw NotImplementedError()
    }

    @Quando("o usuário finaliza o pedido")
    fun `o usuario finaliza o pedido`() {
        throw NotImplementedError()
    }

    @Entao("o status do pedido deve ser alterado para \"Finalizado\"")
    fun `o status do pedido deve ser alterado para Finalizado`() {
        throw NotImplementedError()
    }

    @After("@CleanupOrderFeature")
    fun cleanUpDatabase() {
        log.info {
            """
                ######################################
                Cleaning up Database Order Feature
                ######################################
            """.trimIndent()
        }
        if (createdUserIds.isNotEmpty()) {
            UserService.deleteUsers(createdUserIds)
            createdUserIds.clear()
        }
    }
}
