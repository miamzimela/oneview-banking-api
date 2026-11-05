# OneView Banking API

A Java Spring Boot REST API that aggregates, categorises, and analyses financial transactions from multiple mock Capitec Bank data sources, providing a consolidated view of customer spending and financial activity.

## Project Overview

OneView Banking API is a backend application developed as part of a Software Engineer technical assessment.

The application consolidates customer transaction data from three mock Capitec data sources into an H2 database, supports transaction management through REST endpoints, and provides spending analysis by customer, category, and month.

The project demonstrates Java backend development, RESTful API design, database persistence, multi-source data aggregation, automated testing, Docker containerisation, and continuous integration.

**Note:** All transaction data is fictional and used for demonstration purposes. The application does not connect to Capitec Bank's production systems.

## Technologies Used

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- H2 Database
- Maven
- JUnit 5
- Mockito
- Docker
- GitHub Actions
- Git and GitHub

## Features Implemented

### Transaction Management

- Create financial transactions through REST endpoints
- Retrieve all transactions
- Retrieve transactions by transaction ID
- Retrieve transactions by customer ID
- Persist transactions using Spring Data JPA and H2
- Return appropriate HTTP response codes

### Multi-Source Transaction Aggregation

The application imports transactions from three mock Capitec data sources:

- `capitec-payments.json`
- `capitec-transactions.json`
- `capitec-transfers.json`

These files are stored in `src/main/resources/mock-data/`.

The import process consolidates records into the database and checks transaction identifiers to avoid duplicate imports.

### Automatic Transaction Categorisation

The application supports merchant-based categorisation, including:

- Groceries
- Food & Dining
- Transport
- Entertainment
- Shopping
- Utilities
- Telecommunications
- Healthcare
- Other

The mock data also includes categories such as Housing, Education, Insurance, Transfers, and Savings.

### Spending Analysis

The API provides:

- Customer transaction history
- Total spending calculations
- Spending grouped by category
- Monthly spending calculations
- Identification of the highest spending category

Savings transfers are excluded from spending calculations so that internal savings movements are not treated as expenses.

## REST API Endpoints

### Transaction Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/transactions` | Create a transaction |
| GET | `/api/transactions` | Retrieve all transactions |
| GET | `/api/transactions/{transactionId}` | Retrieve a transaction by ID |
| GET | `/api/transactions/customer/{customerId}` | Retrieve transactions for a customer |

### Spending Analysis Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/spending/{customerId}/transactions` | Retrieve customer transactions |
| GET | `/api/spending/{customerId}/total` | Calculate total spending |
| GET | `/api/spending/{customerId}/categories` | Retrieve spending by category |
| GET | `/api/spending/{customerId}/monthly?year=2026&month=11` | Calculate monthly spending |
| GET | `/api/spending/{customerId}/highest-category` | Identify the highest spending category |

## Example Transaction

```json
{
  "transactionId": "TXN001",
  "customerId": "CUST001",
  "merchant": "Checkers",
  "amount": 250.00,
  "category": "Groceries",
  "source": "Capitec",
  "transactionDate": "2026-11-03"
}
```

## Running the Application Locally

### Prerequisites

- Java 21
- Maven
- Git

An IDE such as IntelliJ IDEA is optional.

### Instructions

**1. Clone the repository**

```bash
git clone https://github.com/miamzimela/oneview-banking-api.git
```

**2. Navigate to the project directory**

```bash
cd oneview-banking-api
```

**3. Run the automated tests**

```bash
mvn clean test
```

**4. Start the application**

```bash
mvn spring-boot:run
```

**5. Access the API**

```text
http://localhost:8080/api/transactions
```

The application automatically imports the bundled mock transaction data during startup.

## Example API Requests

Retrieve all transactions for customer CUST001:

```bash
curl http://localhost:8080/api/spending/CUST001/transactions
```

Retrieve total spending:

```bash
curl http://localhost:8080/api/spending/CUST001/total
```

Retrieve spending by category:

```bash
curl http://localhost:8080/api/spending/CUST001/categories
```

Retrieve spending for November 2026:

```bash
curl "http://localhost:8080/api/spending/CUST001/monthly?year=2026&month=11"
```

Retrieve the highest spending category:

```bash
curl http://localhost:8080/api/spending/CUST001/highest-category
```

## Running with Docker

### Prerequisites

- Docker Engine or Docker Desktop

Docker is not required to run the application locally using Maven.

### Build the Docker Image

From the project root directory:

```bash
docker build -t oneview-banking-api .
```

### Run the Docker Container

```bash
docker run --rm -p 8080:8080 oneview-banking-api
```

Once the application starts, access:

```text
http://localhost:8080/api/spending/CUST001/total
```

The Dockerfile uses a multi-stage build with Maven and Java 21 to package the application and a Java 21 runtime image to execute it.

## Automated Testing

The project includes automated tests covering:

- Transaction service operations
- Transaction categorisation
- Spending analysis calculations
- REST controllers
- Repository persistence
- Integration behaviour
- Spring Boot application context

Run the complete test suite using:

```bash
mvn clean test
```

### Latest Verified Local Test Results

| Metric | Result |
|---|---|
| Tests executed | 50 |
| Tests passed | 50 |
| Failures | 0 |
| Errors | 0 |
| Skipped | 0 |
| Build status | SUCCESS |

## Continuous Integration with GitHub Actions

The project uses GitHub Actions to automate testing and Docker verification.

The workflow is defined in:

`.github/workflows/docker-build.yml`

The workflow runs on pushes and pull requests targeting the `main` branch and can also be started manually.

It performs the following steps:

1. Checks out the repository
2. Configures Java 21
3. Runs the Maven test suite
4. Builds the Docker image
5. Starts the application in a container
6. Verifies that the spending API endpoint responds

The initial GitHub Actions workflow run completed successfully.

## Project Structure

```text
oneview-banking-api/
├── .github/
│   └── workflows/
│       └── docker-build.yml
├── src/
│   ├── main/
│   │   ├── java/com/oneview/banking/
│   │   │   ├── controller/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   └── resources/
│   │       └── mock-data/
│   │           ├── capitec-payments.json
│   │           ├── capitec-transactions.json
│   │           └── capitec-transfers.json
│   └── test/
│       └── java/com/oneview/banking/
├── Dockerfile
├── pom.xml
└── README.md
```

## Future Enhancements

Potential improvements include:

- Pagination and advanced transaction filtering
- OpenAPI/Swagger documentation
- Authentication and authorisation
- Expanded transaction categorisation rules
- Additional financial reporting capabilities
- Persistent database support for production deployments
- Enhanced API error handling and validation

## Project Status

**Core implementation complete — further enhancements possible.**

The application currently supports transaction management, mock data aggregation, categorisation, spending analysis, automated testing, and Docker-based deployment.

The complete local Maven test suite and the initial GitHub Actions workflow have passed.

## Author

GitHub: [miamzimela](https://github.com/miamzimela)

Repository: [oneview-banking-api](https://github.com/miamzimela/oneview-banking-api)