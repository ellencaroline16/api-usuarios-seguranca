# API de Gestão de Usuários — Sistema Seguro

A proposta era construir uma API REST para gerenciar usuários com autenticação JWT, controle de acesso por perfil e boas práticas de segurança, além de uma interface web simples para testar tudo isso na prática.

## Tecnologias utilizadas: 
- Java 21
- Spring Boot 4.1.1 (Web, Security, Data JPA, Validation)
- Banco H2 (em memória)
- JWT (biblioteca jjwt)
- Bcrypt para hash de senha
- HTML, CSS e JavaScript puro no front-end (sem framework)
- Maven

## Instalação:
Pré-requisitos: JDK 21 instalado e Maven (o projeto já vem com o wrapper 'mvnw', então não precisa instalar Maven separado).
1. Clone o repositório: ## git clone https://github.com/ellencaroline16/api-usuarios-seguranca.git

2. Entre na pasta do projeto: cd apiusuarios

## Execução:
No terminal, dentro da pasta do projeto: ./mvnw spring-boot:run

O servidor sobe na porta 8080. A aplicação front-end já vem junto, é só acessar: http://localhost:8080

O banco de dados é em memória (H2) — isso significa que ele reinicia zerado toda vez que o servidor é reiniciado. Pra testar, é preciso cadastrar pelo menos um usuário antes de tentar fazer login.

Pra consultar o banco diretamente (opcional): 'http://localhost:8080/h2-console', com a JDBC URL 'jdbc:h2:mem:apiusuarios', usuário 'sa' e senha em branco.

## Testes: 

### Pela interface web: 
Acesse 'http://localhost:8080', cadastre um usuário (qualquer perfil) e faça login. Dependendo do perfil escolhido, o sistema libera funções diferentes:
- **Admin**: vê todos os usuários, edita, exclui e cadastra novos
- **Operador**: vê todos os usuários e edita, mas não exclui
- **Cliente**: vê apenas os próprios dados, sem opção de editar ou excluir

### Pelo Postman:
A coleção com todas as requisições prontas está em 'postman/apiusuarios.postman_collection.json'. É só importar no Postman.

Endpoints disponíveis:

| Método | Endpoint | Finalidade | Acesso |
|---|---|---|---|
| POST | '/usuarios/cadastro' | Criar usuário | Público |
| POST | '/auth/login' | Autenticar e gerar token | Público |
| GET | '/usuarios' | Listar todos | Admin, Operador |
| GET | '/usuarios/{id}' | Buscar por id | Admin, Operador, ou o próprio usuário |
| PUT | '/usuarios/{id}' | Atualizar nome/email | Admin, Operador |
| DELETE | '/usuarios/{id}' | Excluir | Admin |

Rotas protegidas exigem o header 'Authorization: Bearer <token>', com o token obtido no login.

## Estrutura do projeto:
  src/main/java/br/com/startup/apiusuarios/
├── config (configuração de segurança)
├── controller (endpoints REST)
├── dto (objetos de entrada e saída da API)
├── exception (exceções customizadas)
├── model (entidade Usuario e enum Perfil)
├── repository (acesso ao banco)
├── security (JWT: geração, validação, filtro)
└── service (regras de negócio)
