package io.github.soat7.myburgercontrol.testbdd

import io.cucumber.java.After
import io.cucumber.java.pt.Dado
import io.cucumber.java.pt.Entao
import io.cucumber.java.pt.Quando
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.serpro69.kfaker.faker
import io.github.soat7.myburgercontrol.testbdd.dto.UserRole
import io.github.soat7.myburgercontrol.testbdd.service.AuthService
import io.github.soat7.myburgercontrol.testbdd.service.UserService
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Then
import io.restassured.response.Response
import org.apache.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import java.util.UUID

private val log = KotlinLogging.logger { }

class AutorizacaoUserServiceSteps {

    private val userService = UserService

    private val cpf = "123"
    private val password = "123"
    private val unExistingUserUUID = UUID.randomUUID()

    private lateinit var response: Response
    private lateinit var createdUser: UserService.UserResponse
    private lateinit var accessToken: String

    private val faker = faker { this.fakerConfig { locale = "pt-BR" } }

    companion object {
        private val createdUserIds = mutableListOf<String>()
    }

    @Quando("o usuário se cadastrar com um email válido e uma senha forte")
    fun `o usuario se cadastrar com um email valido e uma senha forte`() {
        response = userService.createUser(
            cpf = cpf,
            password = password,
            userRole = UserRole.USER,
        )
    }

    @Entao("o sistema cria um novo usuário com as informações fornecidas")
    fun `o sistema cria um novo usuario com as informacoes fornecidas`() {
        val createUserResponse = response Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            path<UserService.UserResponse>("content")
        }

        assertThat(createUserResponse).isNotNull
        assertThat(createUserResponse.cpf).isEqualTo(cpf)
        assertThat(createUserResponse.role).isEqualTo(UserRole.USER)
        createdUser = createUserResponse
        createdUserIds.add(createUserResponse.id.toString())
    }

    @Dado("que o usuário existe no banco de dados")
    fun `que o usuario existe no banco de dados`() {
        if (!UserService.isUserCreated(cpf)) {
            UserService.updateAccessToken(null)
            createdUser = userService.createUser(
                cpf = cpf,
                password = password,
                userRole = UserRole.USER,
            ) Then {
                statusCode(HttpStatus.SC_ACCEPTED)
            } Extract {
                path("content")
            }
            createdUserIds.add(createdUser.id.toString())
        }

        accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE3MzMyNzU0MDIsInN1YiI6IjEyMyJ9.GmGtkWLZahfmI-wJHlcC_aiijzk0p-I6Cvsr7Gw76wI"//AuthService.loginAccessToken(cpf, password)
        UserService.updateAccessToken(accessToken)

        createdUser = userService.findUserByCpf(cpf) Then {
            statusCode(HttpStatus.SC_ACCEPTED)
        } Extract {
            path("content")
        }
    }

    @Quando("o usuário realiza a busca pelo seu ID")
    fun `o usuario realiza a busca pelo seu ID`() {
        response = userService.findUserByID(createdUser.id)
        println("BODY DO CALALHO " + response.body())
    }

    @Entao("o sistema retorna as informações do usuário correspondente ao ID informado")
    fun `o sistema retorna as informacoes do usuario correspondente ao ID informado`() {
        println("BODY DO CALALHO " + response.body())
        response Then {
            statusCode(HttpStatus.SC_ACCEPTED)
            log().all()
            body("id", equalTo(createdUser.id.toString()))
            body("cpf", equalTo(cpf))
            body("role", equalTo(UserRole.USER.toString()))
        }
    }

    @Dado("que o usuário não existe no banco de dados")
    fun `que o usuario nao existe no banco de dados`() {
        if (!UserService.isUserCreated(cpf)) {
            UserService.updateAccessToken(null)
            createdUser = userService.createUser(
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
        UserService.updateAccessToken(accessToken)
    }

    @Quando("o usuário realiza a busca por um ID inexistente")
    fun `o usuario realiza a busca por um ID inexistente`() {
        response = userService.findUserByID(unExistingUserUUID)
    }

    @Entao("o sistema retorna uma mensagem de erro indicando que o usuário não foi encontrado")
    fun `o sistema retorna uma mensagem de erro indicando que o usuario nao foi encontrado`() {
        response Then {
            statusCode(HttpStatus.SC_NOT_FOUND)
        }
    }

    @Quando("o usuário realiza a busca pelo seu CPF")
    fun `o usuario realiza a busca pelo seu CPF`() {
        response = userService.findUserByCpf(cpf)
    }

    @Entao("o sistema retorna as informações do usuário correspondente ao CPF informado")
    fun `o sistema retorna as informacoes do usuario correspondente ao CPF informado`() {
        response Then {
            statusCode(HttpStatus.SC_OK)
            log().all()
            body("id", equalTo(createdUser.id.toString()))
            body("cpf", equalTo(cpf))
            body("role", equalTo(UserRole.USER.toString()))
        }
    }

    @Quando("o usuário realiza a busca por um CPF inexistente")
    fun `o usuario realiza a busca por um CPF inexistente`() {
        val randomCpf = faker.string.numerify("#########")
        response = userService.findUserByCpf(randomCpf)
    }

    @Quando("o usuário realiza login com seu email e senha válidos")
    fun `o usuario realiza login com seu email e senha validos`() {
        response = AuthService.loginCall(cpf, password)
    }

    @Entao("o sistema autentica o usuário e retorna um token de acesso")
    fun `o sistema autentica o usuario e retorna um token de acesso`() {
        response Then {
            statusCode(HttpStatus.SC_OK)
            log().all()
            body("access_token", notNullValue())
        }
    }

    @Quando("o usuário realiza login com seu email e senha inválidos")
    fun `o usuario realiza login com seu email e senha invalidos`() {
        response = AuthService.loginCall(cpf, "222")
    }

    @Entao("o sistema rejeita a solicitação de login e retorna uma mensagem de erro indicando que as credenciais são inválidas")
    fun `o sistema rejeita a solicitacao de login e retorna uma mensagem de erro indicando que as credenciais sao invalidas`() {
        response Then {
            statusCode(HttpStatus.SC_UNAUTHORIZED)
        }
    }

    @After("@CleanupAuthUserFeature")
    fun cleanUpDatabase() {
        log.info {
            """
                ######################################
                Cleaning up Database Auth User Feature
                ######################################
            """.trimIndent()
        }
        if (createdUserIds.isNotEmpty()) {
            UserService.deleteUsers(createdUserIds)
            createdUserIds.clear()
        }
    }
}
