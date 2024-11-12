# language: pt
@CleanupAuthUserFeature
# @Ignorar
Funcionalidade: Serviço de Autorização e Criação de Usuário

  # Cenários de criação de usuários e buscas

  Cenário: Criar usuário com sucesso
    Quando o usuário se cadastrar com um email válido e uma senha forte
    Então o sistema cria um novo usuário com as informações fornecidas


  Cenário: Buscar usuário por ID
    Dado que o usuário existe no banco de dados
    Quando o usuário realiza a busca pelo seu ID
    Então o sistema retorna as informações do usuário correspondente ao ID informado

  Cenário: Buscar usuário inexistente por ID
    Dado que o usuário não existe no banco de dados
    Quando o usuário realiza a busca por um ID inexistente
    Então o sistema retorna uma mensagem de erro indicando que o usuário não foi encontrado


  Cenário: Buscar usuário por CPF
    Dado que o usuário existe no banco de dados
    Quando o usuário realiza a busca pelo seu CPF
    Então o sistema retorna as informações do usuário correspondente ao CPF informado

  Cenário: Buscar usuário por CPF inexistente
    Dado que o usuário não existe no banco de dados
    Quando o usuário realiza a busca por um CPF inexistente
    Então o sistema retorna uma mensagem de erro indicando que o usuário não foi encontrado

  # Cenários de autenticação

  Cenário: Usuário autentica no sistema
    Dado que o usuário existe no banco de dados
    Quando o usuário realiza login com seu email e senha válidos
    Então o sistema autentica o usuário e retorna um token de acesso

  Cenário: Usuário não autentica no sistema
    Dado que o usuário não existe no banco de dados
    Quando o usuário realiza login com seu email e senha inválidos
    Então o sistema rejeita a solicitação de login e retorna uma mensagem de erro indicando que as credenciais são inválidas
