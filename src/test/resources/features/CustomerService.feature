# language: pt

@CleanupCustomerFeature
Funcionalidade: Serviço de Criação e Busca de Cliente

  Contexto:
    Dado que o usuário esteja cadastrado

  Cenário: Criar um novo cliente
    Quando o usuário se cria como um novo cliente
    Então o sistema deve criar um novo cliente com os dados fornecidos

  Cenário: Buscar um cliente existente por ID
    Dado que o cliente já tenha sido criado
    Quando o usuário busca um cliente pelo seu ID
    Então o sistema deve retornar as informações do cliente correspondente

  Cenário: Buscar um cliente inexistente por ID
    Quando o usuário busca um cliente com um ID inexistente
    Então o sistema deve retornar cliente não encontrado

  Cenário: Buscar um cliente existente por CPF
    Dado que o cliente já tenha sido criado
    Quando o usuário busca um cliente pelo seu CPF
    Então o sistema deve retornar as informações do cliente correspondente

  Cenário: Buscar um cliente inexistente por CPF
    Quando o usuário busca um cliente pelo seu CPF
    Então o sistema deve retornar cliente não encontrado
