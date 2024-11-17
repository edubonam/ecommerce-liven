# Projeto E-commerce

API HTTP para um e-commerce. Gerenciamento de usuários, produtos e carrinhos de compras.

## Tecnologias Utilizadas

- Java 17
- Spring Boot
- Groovy
- PostgreSQL
- Docker

## Descrição

Sistema desenvolvido em Java 17, Spring Boot e Groovy. Com cobertura de Testes Automatizados

### Base de Dados

- Utilizado PostgreSQL. Por ser uma ferramenta que necessita de instalação, foi configurado um Docker-compose.
- Diagrama entidade-relacionamento.

![ecommerce_db](https://github.com/user-attachments/assets/0b89bb6c-d6f9-4ec2-8527-a45743d4e6a6)

  

## Configuração do Docker-compose

Para iniciar o serviço do banco de dados PostgreSQL usando Docker, utilize o seguinte comando:

```sh
docker-compose up -d
```

## Autenticação

- Utilizado o Spring Security para gerenciar a autenticação.

## Endpoints

- Mapeamento realizado pelo Swagger.

### Usuários/Conta

#### Cadastro de Usuário

- **Endpoint**: [POST] `/api/auth/create-user`
- **Requisição**:
  - Foram criadas 2 Roles (ADMIN e USER), os acessos são os mesmos para ambas.
  - Necessita de um JSON no formato abaixo com os dados do usuário:

```json
{
    "name": "userName",
    "password": "userPass",
    "email": "user@email.com",
    "role": "ADMIN"
}
```

#### Obtenção do Token

- **Endpoint**: [POST] `/api/auth/login`
- **Requisição**:
  - Necessita de um JSON no formato abaixo com os dados de login do usuário:

```json
{
    "email": "user@email.com",
    "password": "userPass"
}
```

### Produtos

#### Cadastro de Produto

- **Endpoint**: [POST] `/api/products`
- **Requisição**:
  - Necessita de um JSON no formato abaixo com os dados do produto a ser cadastrado:

```json
{
    "sku": "sku-1",
    "name": "first-product",
    "price": 10.5,
    "quantity": 1
}
```

#### Atualização de Produto

- **Endpoint**: [POST] `/api/products`
- **Requisição**:
  - Necessita de um JSON no formato abaixo com os dados do produto a ser atualizado:

```json
{
    "id": 1,
    "sku": "sku-1",
    "name": "first-updated-product",
    "price": 12.5,
    "quantity": 10
}
```

#### Listagem de Produtos

- **Endpoint**: [GET] `/api/products/page`

### Carrinho de Compras

#### Criação de Carrinho de Compras

- **Endpoint**: [POST] `/api/cart`
- **Requisição**:
  - Necessita de um JSON no formato abaixo com os dados do carrinho a ser cadastrado:

```json
{
    "user": {
        "id": 3
    },
    "products": [],
    "order": null
}
```

#### Edição de Produtos no Carrinho de Compras

- **Endpoint**: [PUT] `/api/cart/{id}/products`
- **Requisição**:
  - Necessita de um JSON no formato abaixo:

```json
{
    "userId": 3,
    "products": [
        {
            "productId": 1,
            "quantity": 2
        },
        {
            "productId": 3,
            "quantity": 1
        }
    ]
}
```

#### Obtenção de Carrinho de Compras pelo ID

- **Endpoint**: [GET] `/api/cart/{id}?userId={user_id}`

#### Checkout

- **Endpoint**: [POST] `/api/cart/{id}/checkout?userId={user_id}`
