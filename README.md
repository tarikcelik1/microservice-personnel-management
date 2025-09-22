# Personel Management System

A comprehensive microservices-based personnel management system built with Spring Boot backend services and React TypeScript frontend. The system provides full CRUD operations for personnel management with real-time notifications via RabbitMQ and email alerts.

##  Architecture Overview

This project follows a microservices architecture pattern with the following components:
- **Personal Backend**: Main CRUD operations for personnel management
- **Notification Backend**: Asynchronous notification service for personnel changes
- **Personnel Frontend**: Modern React TypeScript SPA for user interface

```
┌─────────────────────┐    ┌──────────────────────┐    ┌─────────────────────┐
│   Frontend (React)  │◄──►│  Personal Backend    │◄──►│      Database       │
│     Port: 3000      │    │    Port: 8080        │    │    (H2 In-Memory)   │
└─────────────────────┘    └──────────────────────┘    └─────────────────────┘
                                      │
                                      ▼
                            ┌──────────────────────┐
                            │     RabbitMQ         │
                            │    Port: 5672        │
                            └──────────────────────┘
                                      │
                                      ▼
                            ┌──────────────────────┐    ┌─────────────────────┐
                            │ Notification Backend │◄──►│      Database       │
                            │    Port: 8081        │    │    (H2 In-Memory)   │
                            └──────────────────────┘    └─────────────────────┘
                                      │
                                      ▼
                            ┌──────────────────────┐
                            │    Email Service     │
                            │ (Optional - SMTP)    │
                            └──────────────────────┘
```

## 📁 Project Structure

```
├── README.md
├── Staj_Raporu.md
├── notification-backend/           # Notification Microservice
│   ├── pom.xml
│   ├── README.md
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/notification_backend/
│   │   │   │   ├── NotificationBackendApplication.java
│   │   │   │   ├── config/
│   │   │   │   │   ├── RabbitMQConfig.java
│   │   │   │   │   └── SwaggerConfig.java
│   │   │   │   ├── controller/
│   │   │   │   │   └── NotificationController.java
│   │   │   │   ├── dto/
│   │   │   │   │   ├── NotificationRequest.java
│   │   │   │   │   └── PersonelNotificationDTO.java
│   │   │   │   ├── entity/
│   │   │   │   │   └── NotificationLog.java
│   │   │   │   ├── listener/
│   │   │   │   │   └── PersonelNotificationListener.java
│   │   │   │   ├── repository/
│   │   │   │   │   └── NotificationLogRepository.java
│   │   │   │   └── service/
│   │   │   │       ├── EmailService.java
│   │   │   │       └── NotificationService.java
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       └── templates/
│   │   │           └── personel-notification.html
│   │   └── test/
│   └── target/
├── personal-backend/               # Main Backend Microservice
│   ├── pom.xml
│   ├── HELP.md
│   ├── mvnw & mvnw.cmd
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/personal_backend/
│   │   │   │   ├── PersonalBackendApplication.java
│   │   │   │   ├── aspect/
│   │   │   │   │   └── LoggingAspect.java
│   │   │   │   ├── config/
│   │   │   │   │   ├── RabbitMQConfig.java
│   │   │   │   │   ├── SecurityConfig.java
│   │   │   │   │   └── SwaggerConfig.java
│   │   │   │   ├── controller/
│   │   │   │   │   └── PersonelController.java
│   │   │   │   ├── dto/
│   │   │   │   │   ├── PersonelCreateDTO.java
│   │   │   │   │   ├── PersonelNotificationDTO.java
│   │   │   │   │   ├── PersonelResponseDTO.java
│   │   │   │   │   └── PersonelUpdateDTO.java
│   │   │   │   ├── entity/
│   │   │   │   │   └── Personel.java
│   │   │   │   ├── exception/
│   │   │   │   │   ├── DuplicateEmailException.java
│   │   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   │   └── PersonelNotFoundException.java
│   │   │   │   ├── repository/
│   │   │   │   │   └── PersonelRepository.java
│   │   │   │   └── service/
│   │   │   │       ├── NotificationService.java
│   │   │   │       └── PersonelService.java
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   └── target/
└── personel-frontend/              # React TypeScript Frontend
    ├── package.json
    ├── tsconfig.json
    ├── vite.config.ts
    ├── tailwind.config.js
    ├── postcss.config.js
    ├── README.md
    ├── public/
    │   └── index.html
    └── src/
        ├── App.tsx
        ├── index.tsx
        ├── index.css
        ├── components/
        │   ├── PersonelForm.tsx
        │   └── PersonelList.tsx
        ├── context/
        │   └── PersonelContext.tsx
        ├── pages/
        ├── services/
        │   └── personelService.ts
        └── types/
            └── personel.ts
```

## 🚀 Technologies Used

### Backend Technologies
- **Java 17**
- **Spring Boot 3.5.5**
- **Spring Data JPA**
- **Spring Security**
- **Spring Web**
- **Spring AMQP (RabbitMQ)**
- **Spring Mail**
- **Spring AOP**
- **Spring Actuator**
- **H2 Database** (In-memory for development)
- **Thymeleaf** (Email templates)
- **Swagger/OpenAPI** (API documentation)
- **Maven** (Build tool)

### Frontend Technologies
- **React 18.2.0**
- **TypeScript 4.9.0**
- **Vite** (Build tool & dev server)
- **Tailwind CSS** (Styling)
- **Axios** (HTTP client)
- **React Hook Form** (Form management)
- **React Router DOM** (Routing)
- **React Hot Toast** (Notifications)
- **Context API** (State management)
- **Lucide React** (Icons)

### Infrastructure & Tools
- **RabbitMQ** (Message broker)
- **Email Service** (Configurable - SMTP/Gmail support available)

##  Features

### Personal Backend Service (Port: 8080)
-  **Complete CRUD Operations** for personnel management
-  **Data Validation** with Bean Validation
-  **Exception Handling** with global exception handler
-  **Spring Security** authentication and authorization
-  **Pagination & Sorting** support
-  **Search & Filtering** capabilities
-  **RabbitMQ Integration** for event publishing
-  **Swagger Documentation** at `/swagger-ui.html`
-  **Health Monitoring** with Actuator

### Notification Backend Service (Port: 8081)
-  **RabbitMQ Consumer** for personnel change events
-  **Email Notifications** with HTML templates
-  **Notification Logging** and tracking
-  **Retry Mechanism** for failed notifications
-  **Template-based Emails** using Thymeleaf
-  **Notification Statistics** and reporting

### Frontend Application (Port: 3000)
-  **Modern React UI** with TypeScript
-  **Responsive Design** with Tailwind CSS
-  **Form Validation** and error handling
-  **Real-time Toast Notifications**
-  **Context API** for state management
-  **Modular Component Architecture**
-  **Type-safe Development** with TypeScript interfaces
-  **REST API Integration** with Axios

## 🔧 Configuration & Setup

### Prerequisites
- **Java 17** or higher
- **Node.js 16** or higher
- **RabbitMQ** server
- **Maven** (for backend builds)
- **npm/yarn** (for frontend)

### Environment Setup

#### 1. RabbitMQ Setup
```bash
# Install RabbitMQ (Windows)
# Download and install from: https://www.rabbitmq.com/download.html

#### 2. Backend Services Configuration

**Personal Backend** (`personal-backend/src/main/resources/application.properties`):
```properties
# Server Configuration
server.port=8080
spring.application.name=personel-backend

# Database Configuration
spring.datasource.url=jdbc:h2:mem:personeldb
spring.h2.console.enabled=true

# RabbitMQ Configuration
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```

### Email Configuration (Optional)
If you want to enable email notifications, update the notification backend configuration:

**Notification Backend** (`notification-backend/src/main/resources/application.properties`):
```properties
# Email Configuration (Optional - Update with your settings)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password

# Application Settings
app.notification.hr-email=hr@company.com
app.notification.from-email=noreply@company.com
```

**Note:** Email functionality is optional. If not configured, the notification service will still log events but won't send emails.

###  Running the Application

#### 1. Start Backend Services

**Personal Backend:**
```bash
cd personal-backend
./mvnw spring-boot:run
# or
mvn spring-boot:run
```

**Notification Backend:**
```bash
cd notification-backend
./mvnw spring-boot:run
# or
mvn spring-boot:run
```

#### 2. Start Frontend Application
```bash
cd personel-frontend
npm install
npm run dev
```

###  API Endpoints

#### Personal Backend API (http://localhost:8080)
- **GET** `/api/personel` - Get all personnel
- **GET** `/api/personel/{id}` - Get personnel by ID
- **POST** `/api/personel` - Create new personnel
- **PUT** `/api/personel/{id}` - Update personnel
- **DELETE** `/api/personel/{id}` - Delete personnel
- **GET** `/api/personel/search` - Search personnel
- **GET** `/api/personel/departman/{departman}` - Get by department

#### Notification Backend API (http://localhost:8081)
- **GET** `/api/notifications` - Get all notification logs
- **GET** `/api/notifications/{id}` - Get notification by ID
- **POST** `/api/notifications/send` - Send manual notification
- **POST** `/api/notifications/retry/{id}` - Retry failed notification

###  API Documentation
- **Personal Backend Swagger:** http://localhost:8080/swagger-ui.html
- **Notification Backend Swagger:** http://localhost:8081/swagger-ui.html

###  Database Access
- **Personal Backend H2 Console:** http://localhost:8080/h2-console
- **Notification Backend H2 Console:** http://localhost:8081/h2-console
  - JDBC URL: `jdbc:h2:mem:personeldb` / `jdbc:h2:mem:notificationdb`
  - Username: `sa`
  - Password: `password`

###  Monitoring & Management
- **Personal Backend Health:** http://localhost:8080/actuator/health
- **Notification Backend Health:** http://localhost:8081/actuator/health
- **RabbitMQ Management:** http://localhost:15672 (guest/guest)

##  Application Flow

1. **User Action:** User performs CRUD operations on the React frontend
2. **API Call:** Frontend makes REST API calls to Personal Backend
3. **Data Processing:** Personal Backend processes the request and updates database
4. **Event Publishing:** Personal Backend publishes events to RabbitMQ
5. **Event Consumption:** Notification Backend consumes events from RabbitMQ
6. **Email Notification:** Notification Backend sends email notifications
7. **Logging:** All operations are logged for audit and monitoring

## Development

### Backend Development
```bash
# Run tests
mvn test

# Build JAR
mvn clean package

# Run with specific profile
mvn spring-boot:run 
```

### Frontend Development
```bash
# Development server
npm run dev

# Build for production
npm run build

# Run tests
npm test

# Type checking
npx tsc --noEmit
```

## 📝 Data Model

### Personnel Entity
```typescript
{
  id: number;
  ad: string;              // First name
  soyad: string;           // Last name
  email: string;           // Email (unique)
  telefon: string;         // Phone number
  departman: string;       // Department
  pozisyon: string;        // Position
  maas: number;           // Salary
  iseBaslamaTarihi: string; // Start date
  aktif: boolean;          // Active status
  olusturmaTarihi: string; // Creation timestamp
  guncellemeTarihi: string; // Update timestamp
}
```
