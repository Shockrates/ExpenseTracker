# Expense Tracker API

## Overview
Expense Tracker is a Spring Boot-based web application that allows users to track their expenses efficiently. The application follows a RESTful architecture, integrates authentication and authorization using JWT tokens, and persists data in a MySQL database.

## Technologies Used
- **Java 21**
- **Spring Boot 3.4.4**
- **Maven 3.9.9**
- **Spring Security (JWT Authentication & Authorization)**
- **MySQL Database**
- **Spring Data JPA**
- **RESTful API with Spring Web**
- **Exception Handling using @RestControllerAdvice**

## Features
- User authentication and authorization with JWT
- CRUD operations for expenses, categories, and users
- Expense tracking by user and category
- Expense filtering by date and price range
- Secure API endpoints with role-based access control
- API documentation with Swagger (available at /swagger-ui.html)

## Installation & Setup
### Prerequisites
Ensure you have the following installed:
- Java 21
- Maven 3.9.9
- MySQL Server

### Clone the Repository
```sh
 git clone https://github.com/your-repo/ExpenseTracker.git
 cd ExpenseTracker
```

### Configure Database
Modify the `application.properties` file to set up MySQL credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/expensetracker
spring.datasource.username=root
spring.datasource.password=yourpassword
```

### Build and Run the Application
```sh
mvn clean install
mvn spring-boot:run
```

## API Endpoints
### Expense Controller
| Method | Endpoint | Description |
|--------|---------|-------------|
| **GET** | `/api/expenses/{id}` | Get Expense by ID |
| **PUT** | `/api/expenses/{id}` | Update an existing expense |
| **DELETE** | `/api/expenses/{id}` | Delete an expense |
| **GET** | `/api/expenses` | Get all expenses |
| **POST** | `/api/expenses` | Create a new expense |
| **GET** | `/api/expenses/user/{userId}` | Get all expenses by user |
| **GET** | `/api/expenses/total` | Get the total expenses |
| **GET** | `/api/expenses/category/{categoryId}` | Get all expenses by category |
| **GET** | `/api/expenses/between-ranges` | Get all expenses between two price ranges |
| **GET** | `/api/expenses/between-dates` | Get all expenses between two dates |

### User Controller
| Method | Endpoint | Description |
|--------|---------|-------------|
| **GET** | `/api/users/{id}` | Get user by ID |
| **PUT** | `/api/users/{id}` | Update existing user |
| **DELETE** | `/api/users/{id}` | Delete a user |
| **PUT** | `/api/users/{id}/password` | Update user password |
| **POST** | `/api/users/register` | Register a user |
| **POST** | `/api/users/login` | Login |
| **GET** | `/api/users` | Get all users |
| **GET** | `/api/users/{id}/total` | Get total expenses of a user |

### Category Controller
| Method | Endpoint | Description |
|--------|---------|-------------|
| **GET** | `/api/categories/{id}` | Get category by ID |
| **PUT** | `/api/categories/{id}` | Update existing category |
| **DELETE** | `/api/categories/{id}` | Delete a category |
| **GET** | `/api/categories` | Get all categories |
| **POST** | `/api/categories` | Register a category |
| **GET** | `/api/categories/{id}/total` | Get total expenses of a category |
| **GET** | `/api/categories/{id}/expenses` | Get category and expenses by category ID |

## Request & Response Examples
### Create Expense Request Body
```json
{
  "expenseAmount": 100.50,
  "expenseDate": "2025-03-28",
  "expenseDescription": "Grocery Shopping",
  "expenseUser": {
    "userId": 1
  },
  "expenseCategory": {
    "categoryId": 2
  }
}
```

### Expense Response Body
```json
{
  "message": "Expense created successfully",
  "data": {
    "expenseId": 1,
    "expenseAmount": 100.50,
    "expenseDate": "2025-03-28",
    "expenseDescription": "Grocery Shopping",
    "categoryName": "Food",
    "userName": "John Doe"
  }
}
```

## Security & Authentication
- JWT-based authentication is implemented to secure API endpoints.
- Users must log in to receive a JWT token, which is required for accessing protected routes.

## License
This project is licensed under the MIT License.

## Contact
For any inquiries or contributions, contact **[Your Name]** at **[Your Email]**.

