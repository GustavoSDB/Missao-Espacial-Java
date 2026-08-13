# Missao Espacial

Projeto migrado de uma aplicacao Java de console para uma API REST com Spring Boot 3.

## Requisitos

- Java 17 ou superior
- Maven 3.6.3 ou superior
- PostgreSQL 12 ou superior


## Como executar

```bash
mvn spring-boot:run
```

No Windows, se o Maven nao estiver no PATH, rode:

```bat
rodar.bat
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

### Consultar foto astronomica da NASA

```bash
curl http://localhost:8080/api/nasa/apod
```

A rota usa a API APOD da NASA. Se `NASA_API_KEY` nao for informada, a aplicacao usa `DEMO_KEY`.

## Rotas disponiveis

- `GET /api/foguetes`
- `POST /api/foguetes`
- `DELETE /api/foguetes/{id}`
- `POST /api/foguetes/{id}/abastecer`
- `GET /api/satelites`
- `POST /api/satelites`
- `DELETE /api/satelites/{id}`
- `POST /api/satelites/{id}/ativar-paineis`
- `POST /api/satelites/{id}/enviar-dados`
- `POST /api/missoes/iniciar`
- `GET /api/status`
- `GET /api/nasa/apod`
