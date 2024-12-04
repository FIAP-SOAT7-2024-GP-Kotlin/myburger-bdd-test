# language: pt


@CleanupAuthUserFeature
Funcionalidade: Serviço de Autorização e Criação de Pagamentos

Cenários de criação de solicitação de pagamentos

  Cenário: Criar solicitação pagamentos com sucesso
    Quando solicitado um pagamento de pedido
    Então o sistema deve retornar as informações de solicitação com o status recebido
