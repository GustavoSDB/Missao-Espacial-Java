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
