package io.github.soat7.myburgercontrol.testbdd

import io.cucumber.java.Before
import io.cucumber.java.pt.Dado
import io.cucumber.java.pt.Entao
import io.cucumber.java.pt.Quando
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.serpro69.kfaker.faker
import io.github.soat7.myburgercontrol.testbdd.dto.UserRole
import io.github.soat7.myburgercontrol.testbdd.service.AuthService
import io.github.soat7.myburgercontrol.testbdd.util.Configuration
import io.restassured.builder.RequestSpecBuilder

private val log = KotlinLogging.logger { }

class ProdutoManagementServiceSteps {
    private val config = Configuration
    private val spec = RequestSpecBuilder()
        .setBaseUri(config["myburger.baseUri"])
        .build()

    private val faker = faker { this.fakerConfig { locale = "pt-BR" } }
    private val cpf = "11111111111"
    private val password = "123"
    private val userRole = UserRole.ADMIN

    @Before
    fun setUp() {
    }

    @Dado("que o usuário cadastrado tenha um papel de ADMINISTRADOR")
    fun `que o usuario cadastrado tenha um papel de ADMINISTRADOR`() {
        log.info { "Dado que o usuario cadastrado tenha um papel de ADMINISTRADOR" }
        log.info { "criando o usuário" }
        val response = try {
            AuthService.loginAccessToken(cpf, password)
        } catch (e: Exception) {
            log.error(e) { e.message }
            error("Erro ao tentar fazer login")
        }
        log.info { "response=$response" }
    }

    @Quando("o usuário cadastrar um novo produto com nome, descrição, preço e tipo")
    fun `o usuario cadastrar um novo produto com nome, descricao, preco e tipo`() {
        // TODO: Implementação do cenário onde o usuário cadastra um novo produto
    }

    @Entao("o sistema deve retornar uma mensagem de sucesso informando que o produto foi cadastrado com sucesso")
    fun `o sistema deve retornar uma mensagem de sucesso informando que o produto foi cadastrado com sucesso`() {
        // TODO: Implementação do cenário onde o sistema retorna uma mensagem de sucesso após cadastrar um novo produto
    }

    @Dado("que existe um produto cadastrado no sistema")
    fun `que existe um produto cadastrado no sistema`() {
        // TODO: Implementação do cenário onde já existe um produto cadastrado no sistema
    }

    @Quando("o usuário atualizar as informações do produto com novo nome, descrição, preço e tipo")
    fun `o usuario atualizar as informaçoes do produto com novo nome, descricao, preco e tipo`() {
        // TODO: Implementação do cenário onde o usuário atualiza as informações de um produto
    }

    @Entao("o sistema deve retornar uma mensagem de sucesso informando que as informações do produto foram atualizadas com sucesso")
    fun `o sistema deve retornar uma mensagem de sucesso informando que as informacoes do produto foram atualizadas com sucesso`() {
        // TODO: Implementação do cenário onde o sistema retorna uma mensagem de sucesso após atualizar um produto
    }

    @Quando("o usuário consultar as informações do produto")
    fun `o usuario consultar as informacoes do produto`() {
        // TODO: Implementação do cenário onde o usuário consulta as informações de um produto
    }

    @Entao("o sistema deve retornar as informações do produto")
    fun `o sistema deve retornar as informacoes do produto`() {
        // TODO: Implementação do cenário onde o sistema retorna as informações de um produto
    }

    @Quando("o usuário consultar as informações de um produto inexistente")
    fun `o usuario consultar as informacoes de um produto inexistente`() {
        // TODO: Implementação do cenário onde o usuário consulta as informações de um produto inexistente
    }

    @Entao("o sistema deve retornar uma mensagem de erro informando que o produto não foi encontrado")
    fun `o sistema deve retornar uma mensagem de erro informando que o produto nao foi encontrado`() {
        // TODO: Implementação do cenário onde o sistema retorna uma mensagem de erro ao consultar um produto inexistente
    }

    @Quando("o usuário consultar todos os produtos cadastrados")
    fun `o usuario consultar todos os produtos cadastrados`() {
        // TODO: Implementação do cenário onde o usuário consulta todos os produtos cadastrados
    }

    @Entao("o sistema deve retornar uma lista com todos os produtos cadastrados")
    fun `o sistema deve retornar uma lista com todos os produtos cadastrados`() {
        // TODO: Implementação do cenário onde o sistema retorna uma lista com todos os produtos cadastrados
    }

    @Quando("o usuário consultar todos os produtos cadastrados de um tipo específico")
    fun `o usuario consultar todos os produtos cadastrados de um tipo especifico`() {
        // TODO: Implementação do cenário onde o usuário consulta todos os produtos cadastrados de um tipo específico
    }

    @Entao("o sistema deve retornar uma lista com todos os produtos cadastrados do tipo específico")
    fun `o sistema deve retornar uma lista com todos os produtos cadastrados do tipo específico`() {
        // TODO: Implementação do cenário onde o sistema retorna uma lista com todos os produtos cadastrados de um tipo específico
    }

    @Quando("o usuário excluir o produto")
    fun `o usuario excluir o produto`() {
        // TODO: Implementação do cenário onde o usuário exclui um produto existente
    }

    @Entao("o sistema deve retornar uma mensagem de sucesso informando que o produto foi excluído com sucesso")
    fun `o sistema deve retornar uma mensagem de sucesso informando que o produto foi excluído com sucesso`() {
        // TODO: Implementação do cenário onde o sistema retorna uma mensagem de sucesso ao excluir um produto
    }

    @Quando("o usuário excluir um produto inexistente")
    fun `o usuario excluir um produto inexistente`() {
        // TODO: Implementação do cenário onde o usuário tenta excluir um produto inexistente
    }
}
