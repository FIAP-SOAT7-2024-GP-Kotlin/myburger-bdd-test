package io.github.soat7.myburgercontrol.testbdd

import io.cucumber.java.After
import io.cucumber.java.Before
import io.cucumber.java.pt.Dado
import io.cucumber.java.pt.Entao
import io.cucumber.java.pt.Quando
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.soat7.myburgercontrol.testbdd.dto.UserRole
import io.github.soat7.myburgercontrol.testbdd.service.UserService
import io.github.soat7.myburgercontrol.testbdd.util.DataSource
import io.restassured.response.Response
import org.apache.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat

private val log = KotlinLogging.logger { }

class AutorizacaoUserServiceSteps {

    private val userService = UserService

    private val cpf = "11111111111"
    private val password = "123"

    private lateinit var response: Response

    companion object {
        private val createdUserIds = mutableListOf<String>()
    }

    @After("@CleanUp")
    fun tearDown() {
        log.info {
            """
                ################
                Cleaning Up User database
                ################
            """.trimIndent()
        }
        DataSource.connection().use { conn ->
            conn.prepareStatement("DELETE FROM myburguer.user WHERE id = ANY (?)")
                .use { stmt ->
                    stmt.setArray(1, conn.createArrayOf("uuid", createdUserIds.toTypedArray()))
                    stmt.execute()
                }
        }
    }

    @Before
    fun setUp() {
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
        val userResponse = response.then()
            .statusCode(HttpStatus.SC_OK)
            .extract()
            .`as`(UserService.UserResponse::class.java)

        assertThat(userResponse)
            .isNotNull

        assertThat(userResponse.cpf).isEqualTo(cpf)
        assertThat(userResponse.role).isEqualTo(UserRole.USER)
        createdUserIds.add(userResponse.id.toString())
    }

    @Dado("que o usuário existe no banco de dados")
    fun `que o usuario existe no banco de dados`() {
        // TODO: Implementação do cenário onde já existe um usuário cadastrado no sistema
    }

    @Quando("o usuário realiza a busca pelo seu ID")
    fun `o usuario realiza a busca pelo seu ID`() {
        // TODO: Implementação do cenário onde o usuário consulta as informações de seu próprio cadastro por ID
    }

    @Entao("o sistema retorna as informações do usuário correspondente ao ID informado")
    fun `o sistema retorna as informacoes do usuario correspondente ao ID informado`() {
        // TODO: Implementação do cenário onde o sistema retorna as informações do usuário consultado por ID
    }

    @Dado("que o usuário não existe no banco de dados")
    fun `que o usuario nao existe no banco de dados`() {
        // TODO: Implementação do cenário onde já existe um usuário cadastrado no sistema
    }

    @Quando("o usuário realiza a busca por um ID inexistente")
    fun `o usuario realiza a busca por um ID inexistente`() {
        // TODO: Implementação do cenário onde o usuário consulta um cadastro com ID inexistente no sistema
    }

    @Entao("o sistema retorna uma mensagem de erro indicando que o usuário não foi encontrado")
    fun `o sistema retorna uma mensagem de erro indicando que o usuario nao foi encontrado`() {
        // TODO: Implementação do cenário onde o sistema retorna uma mensagem de erro ao consultar um ID inexistente
    }

    @Quando("o usuário realiza a busca pelo seu CPF")
    fun `o usuario realiza a busca pelo seu CPF`() {
        // TODO: Implementação do cenário onde o usuário consulta as informações de seu próprio cadastro por CPF
    }

    @Entao("o sistema retorna as informações do usuário correspondente ao CPF informado")
    fun `o sistema retorna as informacoes do usuario correspondente ao CPF informado`() {
        // TODO: Implementação do cenário onde o sistema retorna as informações do usuário consultado por CPF
    }

    @Quando("o usuário realiza a busca por um CPF inexistente")
    fun `o usuario realiza a busca por um CPF inexistente`() {
        // TODO: Implementação do cenário onde o usuário consulta um cadastro com CPF inexistente no sistema
    }

    @Quando("o usuário realiza login com seu email e senha válidos")
    fun `o usuario realiza login com seu email e senha validos`() {
        // TODO: Implementação do cenário onde o usuário realiza login com suas credenciais válidas
    }

    @Entao("o sistema autentica o usuário e retorna um token de acesso")
    fun `o sistema autentica o usuario e retorna um token de acesso`() {
        // TODO: Implementação do cenário onde o sistema autentica o usuário e retorna um token de acesso
    }

    @Quando("o usuário realiza login com seu email e senha inválidos")
    fun `o usuario realiza login com seu email e senha invalidos`() {
        // TODO: Implementação do cenário onde o usuário realiza login com suas credenciais inválidas
    }

    @Entao("o sistema rejeita a solicitação de login e retorna uma mensagem de erro indicando que as credenciais são inválidas")
    fun `o sistema rejeita a solicitacao de login e retorna uma mensagem de erro indicando que as credenciais sao invalidas`() {
        // TODO: Implementação do cenário onde o sistema rejeita a solicitação de login e retorna uma mensagem de erro
    }
}
