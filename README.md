# SoftwareEngineer

API Spring Boot que simula um sistema de pedágio, com foco em veículos e suas tarifas.

## Requisitos

- Java 25 (conforme `pom.xml`)
- PostgreSQL rodando localmente ou remoto
- Maven Wrapper (ja incluso: `./mvnw`)

## Configuracao de banco (PostgreSQL)

O projeto sobe localmente com H2 em memoria, sem precisar de banco externo.

Se voce quiser usar PostgreSQL em ambiente de deploy ou teste manual, defina as variaveis de ambiente previstas em `src/main/resources/application.properties`:

- `DB_URL` (default: `jdbc:postgresql://localhost:5432/postgres`)
- `DB_USER` (default: usuario do sistema operacional)
- `DB_PASSWORD` (default: vazio)

Se quiser definir explicitamente:

```bash
export DB_URL="jdbc:postgresql://localhost:5432/postgres"
export DB_USER="seu_usuario"
export DB_PASSWORD="sua_senha"
```

## Como rodar

### 1) Rodar testes

```bash
./mvnw test
```

### 1.1) Gerar cobertura de teste

```bash
./mvnw verify
```

O relatorio fica em `target/site/jacoco/index.html`.

### 2) Subir a aplicacao

```bash
./mvnw spring-boot:run
```

A aplicacao sobe, por padrao, em `http://localhost:8080`.

### 3) Rodar com Docker

```bash
./mvnw clean package -DskipTests
docker build -t software-engineering-api .
docker run --rm -p 8080:8080 software-engineering-api
```

Se quiser gerar a imagem com tag explicita:

```bash
docker build -t software-engineering-api:1.0 .
docker run --rm -p 8080:8080 software-engineering-api:1.0
```

O deploy em CI/CD publica a imagem Docker no GitHub Container Registry depois que build e testes passam.

## Rodar pelo IntelliJ (Run/Debug)

Crie uma configuracao do tipo **Spring Boot** para a classe:

- `br.inatel.SoftwareEngineer.SoftwareEngineerApplication`

Em **Environment variables**, opcionalmente defina:

- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`

Se nao definir, o projeto usa H2 localmente.

## Endpoints atuais

### GET `/car/vehicles`

Retorna uma string simples.

Teste rapido:

```bash
curl -i "http://localhost:8080/car/vehicles"
```

Esperado:

- Status: `200 OK`
- Body contem: `List of vehicles`

## Como validar endpoints novos (padrao)

Para qualquer endpoint que voce adicionar, valide sempre estes pontos:

1. Metodo HTTP correto (`GET`, `POST`, `PUT`, `DELETE`...)
2. Rota correta (path e parametros)
3. Status HTTP esperado
4. Estrutura do body (JSON/string)
5. Cenarios de erro (400, 404, 500 quando aplicavel)

Template para validar qualquer endpoint com `curl`:

```bash
# GET
curl -i "http://localhost:8080/<rota>"

# POST com JSON
curl -i -X POST "http://localhost:8080/<rota>" \
  -H "Content-Type: application/json" \
  -d '{"campo":"valor"}'

# PUT com JSON
curl -i -X PUT "http://localhost:8080/<rota>/<id>" \
  -H "Content-Type: application/json" \
  -d '{"campo":"novo-valor"}'

# DELETE
curl -i -X DELETE "http://localhost:8080/<rota>/<id>"
```
