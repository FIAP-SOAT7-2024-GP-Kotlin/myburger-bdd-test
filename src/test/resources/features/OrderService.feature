# language: pt
@Ignorar
@CleanupOrderFeature
Funcionalidade:  Serviço de Compra de Produtos

  Contexto:
    Dado que o usuário está logado no sistema

  Cenário: Crie um pedido com produtos
    Quando o usuário fecha um pedido com produtos adicionados
    Então o status do pedido deve ser "Novo"
    E o pedido deve ser criado e os produtos devem estar associados a ele

  Cenário: Lista os pedidos de um cpf
    Dado que existe algum pedido associado ao CPF do usuário
    Quando o usuário solicita a lista de pedidos para o seu CPF
    Então a lista de pedidos deve ser exibida

  Cenário: Lista todos os pedidos
    Dado que existem pedidos no sistema
    Quando o usuário solicita a lista de todos os pedidos
    Então a lista de todos os pedidos deve ser exibida

  Cenário: Lista a fila de pedidos
    Dado que existem pedidos no sistema com status "Novo"
    Quando o usuário solicita a lista da fila de pedidos
    Então a lista da fila de pedidos deve ser exibida
    E o status dos pedidos na fila deve ser "Novo"

  Cenário: Coloca um pedido em Preparação
    Dado que existe um pedido com status "Novo"
    Quando o usuário coloca o pedido em preparação
    Então o status do pedido deve ser alterado para "Em Preparação"

  Cenário: Termina o preparo de um pedido
    Dado que existe um pedido com status "Em Preparação"
    Quando o usuário termina o preparo do pedido
    Então o status do pedido deve ser alterado para "Pronto"

  Cenário: Usuario recebe o pedido
    Dado que existe um pedido com status "Pronto"
    Quando o usuário marca o pedido como recebido
    Então o status do pedido deve ser alterado para "Recebido"

  Cenário: Finaliza o pedido
    Dado que existe um pedido com status "Recebido"
    Quando o usuário finaliza o pedido
    Então o status do pedido deve ser alterado para "Finalizado"
