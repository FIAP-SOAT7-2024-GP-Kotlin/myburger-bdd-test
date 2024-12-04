package io.github.soat7.myburgercontrol.testbdd

import io.cucumber.java.After
import io.cucumber.java.pt.Dado
import io.cucumber.java.pt.Entao
import io.cucumber.java.pt.Quando
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.serpro69.kfaker.faker
import io.github.soat7.myburgercontrol.testbdd.dto.CustomerDTO
import io.github.soat7.myburgercontrol.testbdd.dto.UserRole
import io.github.soat7.myburgercontrol.testbdd.service.AuthService
import io.github.soat7.myburgercontrol.testbdd.service.CustomerService
import io.github.soat7.myburgercontrol.testbdd.service.UserService
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Then
import io.restassured.response.Response
import org.apache.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers.equalTo
import java.util.UUID

private val log = KotlinLogging.logger { }

class CustomerServiceSteps {

    private lateinit var response: Response
    private val cpf = "11111111111"
    private val password = "123"
    private lateinit var accessToken: String
    private lateinit var createdUser: UserService.UserResponse
    private lateinit var inputCustomerDTO: CustomerDTO

    companion object {
        private val createdUserIds = mutableListOf<String>()
        private val createdCustomersIds = mutableListOf<String>()
    }

    private val faker = faker { this.fakerConfig { locale = "pt-BR" } }

    @Dado("que o usuário esteja cadastrado")
    fun `que o usuario esteja cadastrado`() {
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
        CustomerService.updateAccessToken(accessToken)
    }

    @Quando("o usuário se cria como um novo cliente")
    fun `o usuario se cria como um novo cliente`() {
        inputCustomerDTO = CustomerDTO(
            cpf = cpf,
            name = faker.name.name(),
            email = faker.internet.email(),
        )
        response = CustomerService.createCustomer(inputCustomerDTO)
    }

    @Entao("o sistema deve criar um novo cliente com os dados fornecidos")
    fun `o sistema deve criar um novo cliente com os dados fornecidos`() {
        val createdCustomer = response Then {
            statusCode(HttpStatus.SC_OK)
            log().all()
        } Extract {
            path<CustomerDTO>("content")
        }

        assertThat(createdCustomer).isNotNull
        assertThat(createdCustomer.cpf).isEqualTo(inputCustomerDTO.cpf)
        assertThat(createdCustomer.name).isEqualTo(inputCustomerDTO.name)
        assertThat(createdCustomer.email).isEqualTo(inputCustomerDTO.email)

        createdCustomersIds.add(createdCustomer.id.toString())
    }

    @Dado("que o cliente já tenha sido criado")
    fun `que o cliente ja tenha sido criado`() {
        inputCustomerDTO = CustomerDTO(
            cpf = cpf,
            name = faker.name.name(),
            email = faker.internet.email(),
        )
        val createdCustomer = CustomerService.createCustomer(inputCustomerDTO) Then {
            statusCode(HttpStatus.SC_OK)
            log().all()
        } Extract {
            path<CustomerDTO>("content")
        }
        createdCustomersIds.add(createdCustomer.id.toString())
    }

    @Quando("o usuário busca um cliente pelo seu ID")
    fun `o usuario busca um cliente pelo seu ID`() {
        response = CustomerService.getCustomerById(createdCustomersIds.first())
    }

    @Entao("o sistema deve retornar as informações do cliente correspondente")
    fun `o sistema deve retornar as informacoes do cliente correspondentes ao ID fornecido`() {
        response Then {
            log().all()
            statusCode(HttpStatus.SC_OK)
            body("id", equalTo(createdCustomersIds.first()))
            body("cpf", equalTo(inputCustomerDTO.cpf))
            body("name", equalTo(inputCustomerDTO.name))
            body("email", equalTo(inputCustomerDTO.email))
        }
    }

    @Quando("o usuário busca um cliente pelo seu CPF")
    fun `o usuario busca um cliente pelo seu CPF`() {
        response = CustomerService.getCustomerByCPF(cpf)
    }

    @Quando("o usuário busca um cliente com um ID inexistente")
    fun `o usuario busca um cliente com um ID inexistente`() {
        response = CustomerService.getCustomerById(UUID.randomUUID().toString())
    }

    @Entao("o sistema deve retornar cliente não encontrado")
    fun `o sistema deve retornar cliente nao encontrado`() {
        response Then {
            log().all()
            statusCode(HttpStatus.SC_NOT_FOUND)
        }
    }

    @After("@CleanupCustomerFeature")
    fun cleanUpDatabase() {
        log.info {
            """
                ######################################
                Cleaning up Database Customer Feature
                ######################################
            """.trimIndent()
        }
        if (createdUserIds.isNotEmpty()) {
            UserService.deleteUsers(createdUserIds)
            createdUserIds.clear()
        }
        if (createdCustomersIds.isNotEmpty()) {
            CustomerService.deleteCustomers(createdCustomersIds)
            createdCustomersIds.clear()
        }
    }
}
