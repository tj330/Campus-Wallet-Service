# Campus Wallet Service

A Spring Boot REST API that provides a secure campus wallet system for students, campus stores, and administrators.

## Project overview 

Campuses often need a controlled way for students to make purchases inside campus without relying on cash for every transaction. Students need a wallet balance they can use at approved stores, while administrators need a way to manage student accounts, register stores, and review wallet activity.

This service solves that problem by exposing secured REST APIs for wallet operations, student management, store management, and transaction history.


Campus Wallet Service is built as a Spring Boot 3 backend with:

- PostgreSQL persistence for runtime data
- Spring Data JPA entities for students, stores, and transactions
- Stateless JWT authentication
- Role-based access control for admins and students
- Wallet operations with transaction records
- Swagger/OpenAPI documentation for API exploration
- H2-backed tests for validation

The API is designed around a simple workflow: authenticate, manage campus data, perform wallet operations, and inspect balances or transaction history.

## Core Capabilities

- Admins can create, view, update, and delete students.
- Admins can create, view, update, and delete stores.
- Admins and students can access wallet operations.
- Wallet users can deposit, withdraw, pay stores, check balance, and view transaction history.
- Wallet mutation requests reject missing, zero, negative, NaN, and infinite amounts.
- Every deposit, withdrawal, and payment creates a transaction record.

## Authentication and Authorization

The service uses JWT bearer authentication. Basic authentication is disabled.

Public endpoints:

- `POST /auth/login`
- `/swagger-ui/**`
- `/swagger-ui.html`
- `/api-docs`

Default local users:

| Username | Password | Role |
| --- | --- | --- |
| `admin` | `admin123` | `ADMIN` |
| `student` | `student123` | `STUDENT` |

Role access:

| Role | Access |
| --- | --- |
| `ADMIN` | `/students/**`, `/stores/**`, `/wallet/**` |
| `STUDENT` | `/wallet/**` |

JWT settings are configured through application properties:

```properties
jwt.secret=change-this-secret
jwt.expiration-seconds=3600
```

## API Workflow

### 1. Login

```sh
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

Response:

```json
{
  "token": "<jwt-token>",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

### 2. Use the Token

Send the token in the `Authorization` header:

```sh
curl http://localhost:8080/students \
  -H "Authorization: Bearer <jwt-token>"
```

### 3. Manage Campus Data

Admins can manage student and store records:

```sh
curl -X POST http://localhost:8080/students \
  -H "Authorization: Bearer <admin-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "admissionNo": "ST001",
    "name": "Rahul",
    "department": "CS",
    "email": "rahul@campus.edu",
    "balance": 1000.0
  }'
```

### 4. Perform Wallet Operations

```sh
curl -X POST "http://localhost:8080/wallet/deposit?admissionNo=ST001&amount=500" \
  -H "Authorization: Bearer <admin-or-student-token>"
```

```sh
curl -X POST "http://localhost:8080/wallet/pay?admissionNo=ST001&storeId=1&amount=100" \
  -H "Authorization: Bearer <admin-or-student-token>"
```

### 5. Check Balance and History

```sh
curl http://localhost:8080/wallet/balance/ST001 \
  -H "Authorization: Bearer <admin-or-student-token>"
```

```sh
curl http://localhost:8080/wallet/history/ST001 \
  -H "Authorization: Bearer <admin-or-student-token>"
```

## Main Endpoints

### Auth

| Method | Endpoint | Description | Access |
| --- | --- | --- | --- |
| `POST` | `/auth/login` | Authenticate and return JWT | Public |

### Student Management

| Method | Endpoint | Description | Access |
| --- | --- | --- | --- |
| `GET` | `/students` | List students | Admin |
| `GET` | `/students/{admissionNo}` | Get one student | Admin |
| `POST` | `/students` | Create student | Admin |
| `PUT` | `/students/{admissionNo}` | Update student | Admin |
| `DELETE` | `/students/{admissionNo}` | Delete student | Admin |

### Store Management

| Method | Endpoint | Description | Access |
| --- | --- | --- | --- |
| `GET` | `/stores` | List stores | Admin |
| `GET` | `/stores/{storeId}` | Get one store | Admin |
| `POST` | `/stores` | Create store | Admin |
| `PUT` | `/stores/{storeId}` | Update store | Admin |
| `DELETE` | `/stores/{storeId}` | Delete store | Admin |

### Wallet Operations

| Method | Endpoint | Description | Access |
| --- | --- | --- | --- |
| `POST` | `/wallet/deposit` | Add money to wallet | Admin, Student |
| `POST` | `/wallet/withdraw` | Remove money from wallet | Admin, Student |
| `POST` | `/wallet/pay` | Pay a store from wallet balance | Admin, Student |
| `GET` | `/wallet/balance/{admissionNo}` | Check wallet balance | Admin, Student |
| `GET` | `/wallet/history/{admissionNo}` | View wallet transactions | Admin, Student |

## Data Model

The service uses three main domain entities:

- `Student`: campus user identified by admission number, with profile details and wallet balance
- `Store`: campus store identified by store ID, with name and category
- `Transaction`: wallet activity record linked to a student and optionally a store

Database schema:

![Database Schema](images/schema.png)

## API Documentation

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI docs:

```text
http://localhost:8080/api-docs
```

Screenshots:

![API Docs 1](images/Api-doc1.png)

![API Docs 2](images/Api-doc2.png)

## Run the Project

### Run Locally

Configure PostgreSQL in `src/main/resources/application.properties`.

Default database URL:

```text
jdbc:postgresql://localhost:15432/wallet-service
```

Start the application:

```sh
mvn spring-boot:run
```

### Run With Docker

Start PostgreSQL:

```sh
docker compose up -d
```

Build the Spring Boot image:

```sh
docker build -t wallet-service .
```

Run the application container:

```sh
docker run -p 8080:8080 --network campus-wallet-service_wallet-network wallet-service
```

## Testing and Validation

Run the test suite:

```sh
mvn test
```

Current validation result:

```text
Tests run: 30, Failures: 0, Errors: 0, Skipped: 0
```

The tests cover wallet service behavior, invalid wallet amounts, controller security, login, and JWT-protected access.
