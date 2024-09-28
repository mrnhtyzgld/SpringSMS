# SMS Notification Service

This project is a simple **SMS Notification Service** built using **Spring Boot**. The service can handle both individual and bulk SMS notifications, including the use of asynchronous processing, MongoDB integration, error handling, and retry mechanisms for failed notifications.

---

## 🚀 Features
- Send individual and bulk SMS notifications.
- Asynchronous processing
- MongoDB integration using **Spring Data MongoDB**.
- Error handling and notification status tracking.
- Retry mechanism for failed SMS notifications.
- Swagger UI for API documentation and interaction.
- Integration tests using **Testcontainers** to manage MongoDB.

---

## 🛠️ Technologies Used
- **Java** (version 11+)
- **Spring Boot** (Spring Data MongoDB, Spring Web)
- **MongoDB** (with **Testcontainers** for testing)
- **Maven** (for build automation)
- **JUnit**, **Mockito** (for unit testing)
- **Docker** (for containerization)
- **Swagger UI** (for API documentation)

---

## ⚙️ Setup Instructions

### Prerequisites
- **Java 11+** installed on your machine.
- **Maven** installed for managing dependencies and building the project.
- **Docker** installed for running MongoDB using **Testcontainers** or for running the whole project in containers.

### Steps to Run the Application Locally

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/sms-notification-service.git
   cd sms-notification-service
   ```

2. Build the project:
   ```
   mvn clean install
   ```
   
3. Start the Application
   ```
   mvn spring-boot:run
   ```
4. The application will run by default on http://localhost:8080.

## 🌐 API Endpoints

Here are the main API endpoints available in the application. 
The API documentation is also available via Swagger UI at
* http://localhost:8080/swagger-ui/index.html.

### 1. Send Single SMS Notification
- URL: POST /api/v1/notifications/send

- Request Body:
   ```json
    {
     "recipientPhoneNumber": "1234567890",
     "message": "Hello, this is a test SMS!"
    }
  ```
- Response:
   ```
    SMS sent Succesfully
  ```
  Unfortunately response is not JSON.

### 2. Send Bulk SMS Notification
- URL: POST /api/v1/notifications/send-bulk

- Request Body:
   ```json
    {
    "recipientPhoneNumbers": ["1234567890", "0987654321"],
    "message": "This is a bulk SMS!"
    }

  ```
- Response:
   ```
    Bulk SMS sent Succesfully
  ```
  Unfortunately response is not JSON.

## Other

- If MongoDB is not installed locally, the application uses Testcontainers to automatically spin up a MongoDB instance for integration tests.
- The tests will automatically use Testcontainers to spin up a MongoDB instance for integration testing.
  To run the tests, use the following command:
  ```bash
  mvn test
  ```
- Testcontainers ensures that tests are executed in an isolated and controlled environment, allowing for consistent test results.

### 📜 Swagger UI - API Documentation
The API is documented using Swagger, which provides an interactive web interface where you can test the endpoints and view detailed information about each one.

Once the application is running, navigate to http://localhost:8080/swagger-ui/index.html to view the documentation and test the endpoints.

### 🐳 Docker Setup
You can run the entire application, including MongoDB, using Docker. To do this, follow the steps below:

Ensure Docker is installed and running on your machine.
Use the provided Dockerfile and docker-compose.yml to build and run the application.
Run with Docker Compose
Build and start the application with MongoDB:

```bash
docker-compose up --build
```

The application will be accessible at http://localhost:8080 and MongoDB at mongodb://localhost:27017.

To stop the containers, use:

```bash
docker-compose down
```
