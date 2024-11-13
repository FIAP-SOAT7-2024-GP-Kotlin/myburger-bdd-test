# language: pt
@CleanupProductFeature
Funcionalidade:  Serviço de Gerenciamento de Produto

  Essa funcionalidade permite que os usuários cadastram produtos, atualizem e consultem informações sobre eles.

  Contexto:
    Dado que o usuário cadastrado tenha um papel de ADMINISTRADOR


  Cenário: Cadastrar um novo produto
    Quando o usuário cadastrar um novo produto com nome, descrição, preço e tipo
    Então o sistema deve retornar uma mensagem de sucesso informando que o produto foi cadastrado com sucesso

  Cenário: Consultar informações do produto
    Dado que existe um produto cadastrado no sistema
    Quando o usuário consultar as informações do produto
    Então o sistema deve retornar as informações do produto

  Cenário: Consultar informações de um produto inexistente
    Quando o usuário consultar as informações de um produto inexistente
    Então o sistema deve retornar uma mensagem de erro informando que o produto não foi encontrado

  Cenário: Consultar todos os produtos cadastrados
    Dado que existe um produto cadastrado no sistema
    Quando o usuário consultar todos os produtos cadastrados
    Então o sistema deve retornar uma lista com todos os produtos cadastrados

  Cenário: Consultar todos os produtos cadastrados de um tipo específico
    Dado que existe um produto cadastrado no sistema
    Quando o usuário consultar todos os produtos cadastrados de um tipo específico
    Então o sistema deve retornar uma lista com todos os produtos cadastrados do tipo específico

  @Ignorar
  Cenário: Excluir produto
    Dado que existe um produto cadastrado no sistema
    Quando o usuário excluir o produto
    Então o sistema deve retornar uma mensagem de sucesso informando que o produto foi excluído com sucesso

  @Ignorar
  Cenário: Excluir produto inexistente
    Quando o usuário excluir um produto inexistente
    Então o sistema deve retornar uma mensagem de erro informando que o produto não foi encontrado
