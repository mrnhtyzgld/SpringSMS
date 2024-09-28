# SMS Notification Service

This is a simple SMS Notification Service built using Spring Boot. It handles both individual and bulk SMS notifications, supports asynchronous processing, and integrates with MongoDB. The service simulates SMS sending and includes retry mechanisms for failed notifications.

## How to Set Up and Run the Application Locally

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/mrnhtyzgld/SpringSMS.git
   cd SpringSMS
   
2. Build the Project: Ensure you have Java 11 or above and Maven installed.
   ```bash
    mvn clean install

3. Start MongoDB: MongoDB is required to store notifications. You can start MongoDB locally.

4. Run the Application: Start the Spring Boot application:

   ```bash
    mvn spring-boot:run
   
5. Access the Application:

API documentation via Swagger can be accessed at: http://localhost:8080/swagger-ui/index.html.
The default API URL is: http://localhost:8080/api/v1/notifications.



#### c. **API Endpoint Özeti**
Burada API endpoint'lerini ve örnek istekleri/cevapları listele:

## API Endpoints

### 1. Send a Single SMS
**Endpoint:**
- `POST /api/v1/notifications/send`

**Request:**
```json
{
  "recipientPhoneNumber": "1234567890",
  "message": "Your SMS message here"
}
```


Response:

```json
{
  "id": "notification-id",
  "recipientPhoneNumber": "1234567890",
  "message": "Your SMS message here",
  "status": "SENT",
  "timestamp": "2024-09-28T20:15:00"
}
```
2. Send Bulk SMS
***Endpoint:***

POST /api/v1/notifications/send-bulk
Request:

```json
{
  "recipientPhoneNumbers": ["1234567890", "0987654321"],
  "message": "Your bulk SMS message here"
}
```
Response:

```json
{
  "status": "SENT",
  "details": [
    {
      "recipientPhoneNumber": "1234567890",
      "status": "SENT"
    },
    {
      "recipientPhoneNumber": "0987654321",
      "status": "SENT"
    }
  ]
}
```

### 2. **Swagger Entegrasyonu**
Bonus görev olan Swagger entegrasyonu için Swagger’ın kullanıldığını README’ye ekle:

## API Documentation

Swagger is used for API documentation. You can interact with and explore the API via the Swagger UI.

- **Swagger UI:** `http://localhost:8080/swagger-ui/index.html`
- **OpenAPI documentation:** `http://localhost:8080/v3/api-docs`
3. Testcontainers ve Testlerin Çalıştırılması


## Running the Tests with Testcontainers

This project uses Testcontainers for integration tests with MongoDB.

1. **Run the Tests:**
   The tests will automatically spin up a MongoDB instance using Testcontainers.
   ```bash
   mvn test

### 4. **Docker ile Uygulamanın Başlatılması**
Docker'ı kullanarak uygulamayı nasıl başlatacaklarını README'ye ekle:

## Running with Docker

To run the application along with MongoDB using Docker, follow these steps:

1. **Build the Docker image:**
   ```bash
   docker build -t sms-notification-service .
Run the application with MongoDB using Docker Compose:
docker-compose up
