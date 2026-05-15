# Missao Espacial

Projeto migrado de uma aplicacao Java de console para uma API REST com Spring Boot 3.

## Requisitos

- Java 17 ou superior
- Maven 3.6.3 ou superior
- PostgreSQL 12 ou superior

## Banco de dados

Crie o banco no PostgreSQL:

```sql
CREATE DATABASE missao_espacial;
```

Ou execute no Windows:

```bat
criar-banco-postgres.bat
```

O script usa o arquivo `.env`, se ele existir. Se nao existir, ele vai pedir a senha do PostgreSQL.
Use a senha que voce definiu ao instalar o PostgreSQL.

Por padrao, a aplicacao usa:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/missao_espacial
spring.datasource.username=postgres
spring.datasource.password=postgres
```

Se sua senha for diferente, crie um arquivo `.env` na raiz do projeto:

```env
DB_USERNAME=postgres
DB_PASSWORD=sua_senha_do_postgres
DB_URL=jdbc:postgresql://localhost:5432/missao_espacial
```

O arquivo `.env` fica fora do Git porque contem senha. Use `.env.example` como modelo.

Tambem e possivel rodar usando variaveis de ambiente:

```powershell
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="sua_senha"
$env:DB_URL="jdbc:postgresql://localhost:5432/missao_espacial"
.\rodar.bat
```

As tabelas `foguetes`, `satelites` e `satelite_paineis` sao criadas automaticamente pelo Hibernate quando a aplicacao inicia.

## Como executar

```bash
mvn spring-boot:run
```

No Windows, se o Maven nao estiver no PATH, rode:

```bat
rodar.bat
```

No IntelliJ, abra ou recarregue o `pom.xml` como projeto Maven antes de executar.
Depois rode a classe `br.com.missaoespacial.MissaoEspacialApplication`, nao a configuracao antiga `Main`.

Se aparecer `package org.springframework.stereotype does not exist`, o projeto foi aberto como Java puro.
Use `Reload All Maven Projects` na aba Maven ou execute pelo `rodar.bat`.

A aplicacao sobe por padrao em `http://localhost:8080`.

O frontend fica disponivel em:

```text
http://localhost:8080/
```

## Endpoints principais

### Criar foguete

```bash
curl -X POST http://localhost:8080/api/foguetes \
  -H "Content-Type: application/json" \
  -d "{\"nome\":\"Falcon\",\"combustivelRestante\":60,\"cargaMaxima\":1000,\"status\":\"Em solo\"}"
```

### Criar satelite

```bash
curl -X POST http://localhost:8080/api/satelites \
  -H "Content-Type: application/json" \
  -d "{\"nome\":\"Sat-1\",\"massa\":500,\"orbitaAlvo\":\"LEO\",\"energia\":100,\"status\":\"Em solo\"}"
```

### Iniciar missao

```bash
curl -X POST http://localhost:8080/api/missoes/iniciar \
  -H "Content-Type: application/json" \
  -d "{\"sateliteId\":1,\"fogueteId\":1}"
```

### Consultar status

```bash
curl http://localhost:8080/api/status
```

## Rotas disponiveis

- `GET /api/foguetes`
- `POST /api/foguetes`
- `POST /api/foguetes/{id}/abastecer`
- `GET /api/satelites`
- `POST /api/satelites`
- `POST /api/satelites/{id}/ativar-paineis`
- `POST /api/satelites/{id}/enviar-dados`
- `POST /api/missoes/iniciar`
- `GET /api/status`
