# Campus Wallet Service

A Spring Boot 3 RESTful service for managing campus wallet transactions for students and stores.

## Features
- Student, Store, Transaction entities (JPA)
- CRUD APIs for students and stores
- Wallet operations: deposit, withdraw, pay, check balance, transaction history
- MySQL database integration
- DTOs for request/response
- Exception handling
- Swagger/OpenAPI documentation
- Role-based Spring Security (Admin vs Student)

## Getting Started
1. Configure MySQL in `src/main/resources/application.properties`.
2. Build and run the project:
   ```sh
   mvn spring-boot:run
   ```
3. Access Swagger UI at `http://localhost:8080/swagger-ui.html`

## API Endpoints
- `/students` - CRUD for students
- `/stores` - CRUD for stores
- `/wallet/deposit` - deposit money
- `/wallet/withdraw` - withdraw money
- `/wallet/pay` - make a payment at store
- `/wallet/balance/{admissionNo}` - get balance
- `/wallet/history/{admissionNo}` - get transaction history

## Security
- Admin: access to all endpoints
- Student: access to wallet endpoints

## License
MIT
