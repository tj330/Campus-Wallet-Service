# Campus Wallet Service

A Spring Boot 3 REST API for managing campus wallet accounts, store payments, and transaction history for students.

## Current Features

- Student, Store, and Transaction entities using Spring Data JPA
- CRUD APIs for students and stores
- Wallet operations for deposit, withdraw, store payment, balance check, and transaction history
- PostgreSQL runtime database configuration
- H2 test database configuration
- DTOs for API request and response payloads
- Centralized exception handling
- Swagger/OpenAPI documentation
- Stateless JWT authentication with role-based authorization
- Admin and Student in-memory users for local development
- Wallet amount validation for positive finite values

## Security

The API uses JWT bearer authentication. Basic auth is disabled.

Default local users:

| Username | Password | Role |
| --- | --- | --- |
| `admin` | `admin123` | `ADMIN` |
| `student` | `student123` | `STUDENT` |

Role access:

- `ADMIN`: access to `/students/**`, `/stores/**`, and `/wallet/**`
- `STUDENT`: access to `/wallet/**`
- Public: `/auth/login`, Swagger UI, and OpenAPI docs

JWT settings can be configured in application properties:

```properties
jwt.secret=change-this-secret
jwt.expiration-seconds=3600
```

## Login

Request a token:

```sh
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

Use the returned token on protected endpoints:

```sh
curl http://localhost:8080/students \
  -H "Authorization: Bearer <token>"
```

## API Endpoints

- `POST /auth/login` - authenticate and return a JWT
- `/students` - CRUD for students, admin only
- `/stores` - CRUD for stores, admin only
- `POST /wallet/deposit` - deposit money
- `POST /wallet/withdraw` - withdraw money
- `POST /wallet/pay` - make a payment at a store
- `GET /wallet/balance/{admissionNo}` - get wallet balance
- `GET /wallet/history/{admissionNo}` - get transaction history

Wallet mutation endpoints reject missing, zero, negative, NaN, and infinite amounts.

## API Documentation

![API Docs 1](images/Api-doc1.png)

![API Docs 2](images/Api-doc2.png)

Swagger UI is available at:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI docs are available at:

```text
http://localhost:8080/api-docs
```

## Run Locally

Configure PostgreSQL in `src/main/resources/application.properties`, then run:

```sh
mvn spring-boot:run
```

The default runtime database configuration expects PostgreSQL at:

```text
jdbc:postgresql://localhost:15432/wallet-service
```

## Run With Docker

Start the database:

```sh
docker compose up -d
```

Build the Spring Boot image:

```sh
docker build -t wallet-service .
```

Run the app container:

```sh
docker run -p 8080:8080 --network campus-wallet-service_wallet-network wallet-service
```

## Testing

Run the full test suite:

```sh
mvn test
```

Current validation result:

```text
Tests run: 30, Failures: 0, Errors: 0, Skipped: 0
```
