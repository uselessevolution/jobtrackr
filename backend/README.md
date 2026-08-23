# JobTrackr Backend

Backend service for **JobTrackr**, a full-stack job application tracking platform.

The backend is built with **Java 21, Spring Boot, Spring Security, JWT, and MongoDB**. It provides authenticated REST APIs for managing job applications, interviews, reminders, notifications, and dashboard analytics.

---

## Tech Stack

- Java 21
- Spring Boot
- Spring Web
- Spring Security
- JWT Authentication
- Spring Data MongoDB
- Jakarta Bean Validation
- Spring Mail
- OpenAPI / Swagger
- Maven
- JUnit
- Mockito

---

## Features

### Authentication

- User registration and login
- JWT-based authentication
- Password hashing
- Authenticated user context
- User-level resource isolation

All protected resources are scoped to the authenticated user.

Cross-user resource access is treated as resource-not-found rather than exposing the existence of another user's data.

### Job Applications

- Create, read, update, and delete job applications
- Search and filtering
- Sorting
- Pagination
- Application priority
- Skills and job metadata
- Application status workflow
- Status transition validation
- Status history tracking

Supported application statuses include:

```text
SAVED
APPLIED
OA_RECEIVED
PHONE_SCREEN
INTERVIEWING
OFFER
ACCEPTED
REJECTED
WITHDRAWN
```

### Interviews

Interviews are stored as embedded resources within job applications.

Supported operations include:

- Create interview
- View interviews
- Update interview
- Delete interview
- Interview scheduling
- Duration and interview type tracking

Interview resources inherit the ownership rules of their parent job application.

### Reminders

- Create reminders for job applications
- Update and delete reminders
- Reminder lifecycle management
- Scheduled reminder processing
- Retry handling for failed reminders
- Email delivery support
- Delivery history
- MongoDB indexes for scheduler queries

Reminder scheduling is handled by backend scheduler logic rather than by the frontend.

### Notifications

- In-app notifications
- Notification listing
- Unread notification count
- Mark notification as read
- Mark all notifications as read
- User-level notification isolation

### Dashboard

The backend exposes dashboard APIs for operational summaries and analytics.

#### Dashboard Summary

Provides information such as:

- Total applications
- Application counts by status
- Pending reminders
- Unread notifications
- Upcoming interviews
- Upcoming reminders

#### Trend Analytics

Supports configurable date ranges for:

- Applications submitted per day
- Interviews scheduled per day

Missing dates are zero-filled so the API can be consumed directly by frontend charts.

#### Conversion Funnel

Tracks historical recruitment milestones:

```text
Applied
   ↓
Interviewed
   ↓
Offered
   ↓
Accepted
```

The funnel uses application status history rather than relying only on the current status.

This means an application that reached an interview or offer and was later rejected is still correctly counted as having reached that milestone.

The API also calculates:

- Application → Interview conversion rate
- Application → Offer conversion rate
- Application → Accepted conversion rate
- Interview → Offer conversion rate
- Offer → Accepted conversion rate

---

## Architecture

The backend follows a layered architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
MongoDB
```

Additional DTO and Mapper layers separate the REST API contract from MongoDB documents.

Typical request flow:

```text
HTTP Request
    ↓
Controller
    ↓
Validation
    ↓
Service / Business Logic
    ↓
Repository
    ↓
MongoDB
    ↓
Mapper
    ↓
Response DTO
```

MongoDB document classes are not exposed directly as API request or response models.

---

## Security Model

JobTrackr uses JWT authentication through Spring Security.

Protected requests follow the general flow:

```text
Client
   ↓
Authorization: Bearer <JWT>
   ↓
Spring Security
   ↓
Authenticated User
   ↓
Controller
   ↓
Service
   ↓
User-scoped Repository Query
```

Resources such as applications, reminders, notifications, interviews, and dashboard analytics are restricted to the authenticated user.

Secrets such as JWT keys and email credentials must be supplied through environment configuration and must never be committed to Git.

---

## API Documentation

When the backend is running locally, Swagger UI is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

Swagger can be used to:

- Inspect available endpoints
- Test request validation
- Authenticate with JWT
- Test protected endpoints
- Inspect request and response schemas

---

## Configuration

Application configuration is located under:

```text
src/main/resources/
```

Sensitive configuration should be provided using environment variables.

Do **not** commit secrets such as:

```text
JWT secrets
SMTP passwords
Email credentials
Production database credentials
```

to the repository.

---

## Running Locally

### Prerequisites

Install:

- Java 21
- MongoDB
- Git

The project includes the Maven Wrapper, so a separate Maven installation is not required.

### Start MongoDB

Make sure MongoDB is running and accessible using the configuration expected by the application.

### Configure Environment Variables

Configure required secrets and environment-specific values before starting the application.

For example, in PowerShell:

```powershell
$env:JWT_SECRET="your-local-development-secret"
```

Additional environment variables may be required depending on whether email delivery and other optional services are enabled.

### Run the Backend

From the `backend` directory:

```powershell
.\mvnw.cmd spring-boot:run
```

The API will normally be available at:

```text
http://localhost:8080
```

---

## Running Tests

From the `backend` directory:

```powershell
.\mvnw.cmd test
```

A successful test run should finish with:

```text
BUILD SUCCESS
```

Tests should be run before committing backend changes.

---

## Error Handling

The backend uses centralized exception handling and a consistent API error response format.

Validation and business-rule failures are converted into appropriate HTTP responses instead of exposing internal exceptions directly to clients.

Examples include:

```text
400 Bad Request
401 Unauthorized
404 Not Found
409 Conflict
```

Input validation is performed using Jakarta Bean Validation where appropriate.

---

## MongoDB Design

Primary backend data includes:

```text
Users
Job Applications
Reminders
Notifications
```

Interviews and application status history are embedded where appropriate.

Indexes are used for frequently queried fields and scheduler/dashboard access patterns.

Repository queries are designed to preserve authenticated user isolation.

---

## Project Structure

The main backend source code is located under:

```text
src/main/java/com/jobtrackr/backend/
```

Major areas include:

```text
application/
auth/
common/
config/
dashboard/
notification/
reminder/
user/
```

Individual modules generally contain responsibilities such as:

```text
controller/
service/
repository/
model/
dto/
mapper/
```

depending on the needs of the module.

---

## Development Principles

The backend is developed around several core principles:

1. Keep controllers thin and business logic in services.
2. Keep persistence models separate from API DTOs.
3. Validate incoming data at the API boundary.
4. Scope protected resources to the authenticated user.
5. Return `404` for cross-user resource access.
6. Keep secrets outside source control.
7. Preserve consistent API error responses.
8. Maintain status and lifecycle rules in the service layer.
9. Add indexes for important MongoDB query patterns.
10. Run regression tests before committing changes.

---

## Current Status

The backend currently includes the core JobTrackr functionality:

```text
Authentication
Job Application Management
Application Status Workflow
Interview Management
Reminder Scheduling
Email Reminder Delivery
Notification Center
Dashboard Summary
Application & Interview Trend Analytics
Conversion Funnel Analytics
```

Frontend integration is being developed separately under the project's `frontend` directory.

---

## License

This project is currently developed as a portfolio and learning project.