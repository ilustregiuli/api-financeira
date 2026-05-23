# API Financeira

API REST desenvolvida com Java e Spring Boot para gerenciamento de lançamentos financeiros de pequenas empresas.

O projeto permite autenticação de usuários, cadastro de empresas e controle de entradas e saídas financeiras, utilizando uma arquitetura organizada em camadas e boas práticas de desenvolvimento backend.

## Tecnologias utilizadas

* Java
* Spring Boot
* Spring Security
* JWT Authentication
* JPA / Hibernate
* Banco H2
* Maven

## Principais funcionalidades

* Cadastro e autenticação de usuários
* Geração de token JWT
* Controle de lançamentos financeiros
* Isolamento de dados por empresa (multitenancy via aplicação)
* API REST estruturada em camadas

## Objetivo do projeto

Projeto desenvolvido com foco em aprendizado prático de Java Spring, arquitetura backend e construção de APIs REST utilizando conceitos modernos de desenvolvimento.

## Como executar

```bash
# Clonar o repositório
git clone <URL_DO_REPOSITORIO>

# Entrar na pasta
cd <NOME_DO_PROJETO>

# Executar o projeto
./mvnw spring-boot:run
```

O projeto possui exemplo de configuração utilizando banco H2 no arquivo:

```properties
application.properties.example
```

## Status

🚧 Projeto em evolução contínua.
