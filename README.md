# OneView Banking API

A Java Spring Boot REST API designed to manage, categorise, and analyse Capitec Bank financial transactions, providing insights into customer spending patterns and financial activity.
## Project Overview

OneView Banking API is a backend application designed to provide a consolidated view of Capitec Bank customer transactions. The application enables transaction management, spending categorisation, and financial analysis to help users better understand their financial behaviour.

The project demonstrates RESTful API development, database persistence, transaction management, and automated testing using Java and Spring Boot.

## Technologies Used

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- H2 Database
- Maven
- JUnit 5
- Mockito
- Git and GitHub

## Features Implemented

- Create financial transactions
- Retrieve all transactions
- Retrieve transactions by transaction ID
- Retrieve transactions by customer ID
- Store transactions using Spring Data JPA
- Return appropriate HTTP status codes
- Automated controller, service, and repository testing

## REST API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/transactions` | Create a new transaction |
| GET | `/api/transactions` | Retrieve all transactions |
| GET | `/api/transactions/{transactionId}` | Retrieve a transaction by ID |
| GET | `/api/transactions/customer/{customerId}` | Retrieve transactions belonging to a customer |

## Example Transaction

```json
{
  "transactionId": "TXN001",
  "customerId": "CUST001",
  "amount": 250.00,
  "category": "Groceries",
  "merchant": "Checkers",
  "source": "Capitec",
  "transactionDate": "2026-11-03"
}
```

## Running the Application

### Prerequisites

- Java 21
- Maven
- IntelliJ IDEA or another Java IDE

### Steps

1. Clone the repository:

```bash
git clone https://github.com/miamzimela/oneview-banking-api.git
```

2. Navigate into the project:

```bash
cd oneview-banking-api
```

3. Start the Spring Boot application:

```bash
mvn spring-boot:run
```

4. Access the API:

```text
http://localhost:8080/api/transactions
```

## Automated Testing

The project includes unit, controller, repository, and application context tests.

Run the tests using:

```bash
mvn test
```

Latest verified test results:

- Tests executed: 11
- Tests passed: 11
- Failures: 0
- Errors: 0
- Skipped: 0
- Build status: SUCCESS

## Future Enhancements

- Implement Capitec transaction data import and processing
- Generate spending summaries by category
- Provide consolidated financial insights
- Add filtering and reporting functionality
- Improve API documentation and error handling

## Project Status

**In Development**

The core Capitec transaction REST API has been implemented and successfully tested. Additional features, including automatic transaction categorisation, spending analysis, and financial reporting, are planned for future development.

## Author

GitHub: [miamzimela](https://github.com/miamzimela)