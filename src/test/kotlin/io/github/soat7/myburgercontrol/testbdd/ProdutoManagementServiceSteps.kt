package io.github.soat7.myburgercontrol.testbdd

import io.cucumber.java.After
import io.cucumber.java.Before
import io.cucumber.java.pt.Dado
import io.cucumber.java.pt.Entao
import io.cucumber.java.pt.Quando
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.serpro69.kfaker.faker
import io.github.soat7.myburgercontrol.testbdd.dto.ProductDTO
import io.github.soat7.myburgercontrol.testbdd.dto.ProductType
import io.github.soat7.myburgercontrol.testbdd.dto.UserRole
import io.github.soat7.myburgercontrol.testbdd.service.AuthService
import io.github.soat7.myburgercontrol.testbdd.service.ProductService
import io.github.soat7.myburgercontrol.testbdd.service.UserService
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Then
import io.restassured.response.Response
import org.apache.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.Matchers.greaterThanOrEqualTo
import java.math.RoundingMode
import java.util.UUID

private val log = KotlinLogging.logger { }

class ProdutoManagementServiceSteps {
    private lateinit var inputProduct: ProductDTO
    private lateinit var response: Response
    private val cpf = "11111111111"
    private val password = "123"
    private lateinit var accessToken: String
    private lateinit var createdUser: UserService.UserResponse

    companion object {
        private val createdUserIds = mutableListOf<String>()
        private val createdProductIds = mutableListOf<String>()
    }

    private val faker = faker { this.fakerConfig { locale = "pt-BR" } }

    @Before
    fun setUp() {
    }

    @Dado("que o usuário cadastrado tenha um papel de ADMINISTRADOR")
    fun `que o usuario cadastrado tenha um papel de ADMINISTRADOR`() {
        if (!UserService.isUserCreated(cpf)) {
            UserService.updateAccessToken(null)
            createdUser = UserService.createUser(
                cpf = cpf,
                password = password,
                userRole = UserRole.ADMIN,
            ) Then {
                statusCode(HttpStatus.SC_OK)
            } Extract {
                path("content")
            }
            createdUserIds.add(createdUser.id.toString())
        }
        accessToken = AuthService.loginAccessToken(cpf, password)
        ProductService.updateAccessToken(accessToken)
    }

    @Quando("o usuário cadastrar um novo produto com nome, descrição, preço e tipo")
    fun `o usuario cadastrar um novo produto com nome, descricao, preco e tipo`() {
        inputProduct = ProductDTO(
            name = faker.food.dish(),
            description = (1..10).joinToString(" ") { faker.lorem.words() },
            type = ProductType.entries.toTypedArray().random(),
            price = faker.random.nextDouble().let { it * 100 }.toBigDecimal().setScale(2, RoundingMode.HALF_UP),
        )
        response = ProductService.createProduct(inputProduct)
    }

    @Entao("o sistema deve retornar uma mensagem de sucesso informando que o produto foi cadastrado com sucesso")
    fun `o sistema deve retornar uma mensagem de sucesso informando que o produto foi cadastrado com sucesso`() {
        val product = response Then {
            log().all()
            statusCode(HttpStatus.SC_OK)
        } Extract {
            path<ProductDTO>("content")
        }

        assertThat(product.name).isEqualTo(inputProduct.name)
        assertThat(product.description).isEqualTo(inputProduct.description)
        assertThat(product.price).isEqualTo(inputProduct.price)
        assertThat(product.type).isEqualTo(inputProduct.type)

        createdProductIds.add(product.id.toString())
    }

    @Dado("que existe um produto cadastrado no sistema")
    fun `que existe um produto cadastrado no sistema`() {
        inputProduct = ProductDTO(
            name = faker.food.dish(),
            description = (1..10).joinToString(" ") { faker.lorem.words() },
            type = ProductType.entries.toTypedArray().random(),
            price = faker.random.nextDouble().let { it * 100 }.toBigDecimal().setScale(2, RoundingMode.HALF_UP),
        )
        val productDTO = ProductService.createProduct(inputProduct) Then {
            log().all()
            statusCode(HttpStatus.SC_OK)
        } Extract {
            path<ProductDTO>("content")
        }

        createdProductIds.add(productDTO.id.toString())
    }

    @Quando("o usuário consultar as informações do produto")
    fun `o usuario consultar as informacoes do produto`() {
        response = ProductService.getProductById(createdProductIds.first())
    }

    @Entao("o sistema deve retornar as informações do produto")
    fun `o sistema deve retornar as informacoes do produto`() {
        response Then {
            log().all()
            statusCode(HttpStatus.SC_OK)
            body("id", notNullValue())
            body("name", equalTo(inputProduct.name))
            body("description", equalTo(inputProduct.description))
            body("price", equalTo(inputProduct.price.toFloat()))
            body("type", equalTo(inputProduct.type.name))
        }
    }

    @Quando("o usuário consultar as informações de um produto inexistente")
    fun `o usuario consultar as informacoes de um produto inexistente`() {
        response = ProductService.getProductById(UUID.randomUUID().toString())
    }

    @Entao("o sistema deve retornar uma mensagem de erro informando que o produto não foi encontrado")
    fun `o sistema deve retornar uma mensagem de erro informando que o produto nao foi encontrado`() {
        response Then {
            statusCode(HttpStatus.SC_NOT_FOUND)
        }
    }

    @Quando("o usuário consultar todos os produtos cadastrados")
    fun `o usuario consultar todos os produtos cadastrados`() {
        response = ProductService.getProducts()
    }

    @Entao("o sistema deve retornar uma lista com todos os produtos cadastrados")
    fun `o sistema deve retornar uma lista com todos os produtos cadastrados`() {
        response Then {
            log().all()
            statusCode(HttpStatus.SC_OK)
            body("content.size()", greaterThanOrEqualTo(1))
        }
    }

    @Quando("o usuário consultar todos os produtos cadastrados de um tipo específico")
    fun `o usuario consultar todos os produtos cadastrados de um tipo especifico`() {
        response = ProductService.getProductByType(inputProduct.type)
    }

    @Entao("o sistema deve retornar uma lista com todos os produtos cadastrados do tipo específico")
    fun `o sistema deve retornar uma lista com todos os produtos cadastrados do tipo especifico`() {
        response Then {
            log().all()
            statusCode(HttpStatus.SC_OK)
            body("size()", greaterThanOrEqualTo(1))
        }
    }

    @Quando("o usuário excluir o produto")
    fun `o usuario excluir o produto`() {
        response = ProductService.deleteProduct(createdProductIds.first())
    }

    @Entao("o sistema deve retornar uma mensagem de sucesso informando que o produto foi excluído com sucesso")
    fun `o sistema deve retornar uma mensagem de sucesso informando que o produto foi excluido com sucesso`() {
        response Then {
            log().all()
            statusCode(HttpStatus.SC_NO_CONTENT)
        }
    }

    @Quando("o usuário excluir um produto inexistente")
    fun `o usuario excluir um produto inexistente`() {
        response = ProductService.getProductById(UUID.randomUUID().toString())
    }

    @After("@CleanupProductFeature")
    fun cleanUpDatabase() {
        log.info {
            """
                ######################################
                Cleaning up Database Product Feature
                ######################################
            """.trimIndent()
        }
        if (createdUserIds.isNotEmpty()) {
            UserService.deleteUsers(createdUserIds)
            createdUserIds.clear()
        }
        if (createdProductIds.isNotEmpty()) {
            ProductService.deleteProducts(createdProductIds)
            createdProductIds.clear()
        }
    }
}
