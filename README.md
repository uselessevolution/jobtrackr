# JobTrackr

JobTrackr is a full-stack job application tracking platform designed to help users manage their job search in one place.

It provides tools for tracking applications, interview progress, reminders, notifications, and job-search analytics while maintaining strict user-level data isolation.

The project is built as a production-style full-stack application using **React, TypeScript, Java, Spring Boot, Spring Security, JWT, and MongoDB**.

---

## Features

### Authentication & Security

- User registration and login
- JWT-based authentication
- Password hashing with Spring Security
- Protected API endpoints
- Authenticated user context
- Strict user-level resource isolation
- Cross-user resource access returns `404 Not Found`

### Job Application Management

Users can manage the complete lifecycle of their job applications.

Features include:

- Create, view, update, and delete applications
- Search applications
- Filter by application status
- Filter by priority
- Sort applications
- Pagination
- Track job metadata and required skills
- Application deadlines
- Application status workflow
- Historical status tracking

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

### Interview Tracking

Each job application can contain interview records.

Users can:

- Add interviews
- Update interviews
- Delete interviews
- Track interview type
- Schedule interview dates and times
- Track interview duration

Interviews are stored as embedded resources within their parent job applications.

### Reminders

JobTrackr includes a reminder system for important application events.

Features include:

- Create application reminders
- Update and delete reminders
- Scheduled reminder processing
- Reminder lifecycle management
- Retry handling for failed reminders
- Email reminder delivery
- Delivery history tracking

### Notifications

The application includes an in-app notification system.

Users can:

- View notifications
- View unread notification counts
- Mark notifications as read
- Mark all notifications as read

Notifications are isolated to the authenticated user.

---

## Dashboard & Analytics

JobTrackr provides both operational dashboard information and historical job-search analytics.

### Dashboard Summary

The dashboard provides:

- Total applications
- Application counts by status
- Pending reminders
- Unread notifications
- Upcoming interviews
- Upcoming reminders

### Application & Interview Trends

Users can view configurable time-based analytics for:

- Applications submitted per day
- Interviews scheduled per day

Missing dates are automatically zero-filled, making the API suitable for direct frontend chart rendering.

### Conversion Funnel

JobTrackr tracks how applications progress through major recruitment milestones:

```text
Applied
   ↓
Interviewed
   ↓
Offered
   ↓
Accepted
```

Unlike a simple current-status count, the conversion funnel uses application status history.

For example, an application that reached an interview and was later rejected is still correctly counted as having reached the interview stage.

The dashboard calculates:

- Application → Interview conversion rate
- Application → Offer conversion rate
- Application → Accepted conversion rate
- Interview → Offer conversion rate
- Offer → Accepted conversion rate

---

## Tech Stack

### Frontend

- React
- TypeScript
- Vite
- React Router
- Axios

### Backend

- Java 21
- Spring Boot
- Spring Web
- Spring Security
- JWT
- Spring Data MongoDB
- Jakarta Bean Validation
- Spring Mail
- OpenAPI / Swagger
- Maven

### Database

- MongoDB

### Testing & Quality

- JUnit
- Mockito
- Maven Test
- ESLint

Additional automated testing and quality tooling will continue to be expanded as the project develops.

---

## Architecture

JobTrackr follows a full-stack client/server architecture:

```text
┌──────────────────────────┐
│     React Frontend       │
│   React + TypeScript     │
└────────────┬─────────────┘
             │
             │ REST / JSON
             │ JWT
             ▼
┌──────────────────────────┐
│    Spring Boot Backend   │
│                          │
│ Controller               │
│     ↓                    │
│ Service                  │
│     ↓                    │
│ Repository               │
└────────────┬─────────────┘
             │
             ▼
┌──────────────────────────┐
│         MongoDB          │
└──────────────────────────┘
```

The backend separates API contracts from persistence models using DTOs and mappers.

MongoDB document classes are not exposed directly as REST request or response objects.

---

## Security Design

Protected requests use JWT authentication:

```text
React Client
     ↓
POST /api/auth/login
     ↓
JWT
     ↓
Authorization: Bearer <token>
     ↓
Spring Security
     ↓
Authenticated User
     ↓
User-scoped Service / Repository
     ↓
MongoDB
```

Application data is always scoped to the authenticated user.

This applies to:

- Job applications
- Interviews
- Reminders
- Notifications
- Dashboard data
- Analytics

Secrets such as JWT keys, SMTP credentials, and production database credentials are never stored in source control.

---

## Repository Structure

```text
jobtrackr/
│
├── backend/
│   ├── src/
│   ├── pom.xml
│   ├── mvnw
│   ├── mvnw.cmd
│   └── README.md
│
├── frontend/
│   ├── src/
│   ├── package.json
│   └── ...
│
└── README.md
```

### Backend

The Spring Boot backend contains the application's REST APIs, authentication, business logic, persistence, scheduling, notifications, and analytics.

See:

```text
backend/README.md
```

for backend-specific documentation.

### Frontend

The React frontend provides the user interface and communicates with the backend through authenticated REST API requests.

Frontend development is currently in progress.

---

## Running the Project Locally

### Prerequisites

Install:

- Java 21
- Node.js
- npm
- MongoDB
- Git

---

### 1. Start the Backend

Navigate to:

```powershell
cd backend
```

Configure the required local environment variables, including the JWT secret.

Then run:

```powershell
.\mvnw.cmd spring-boot:run
```

The backend normally runs at:

```text
http://localhost:8080
```

Swagger UI is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

---

### 2. Start the Frontend

Open another terminal:

```powershell
cd frontend
npm install
npm run dev
```

The Vite development server normally runs at:

```text
http://localhost:5173
```

---

## Testing

### Backend

Run:

```powershell
cd backend
.\mvnw.cmd test
```

### Frontend

Run:

```powershell
cd frontend
npm run build
npm run lint
```

---

## API Overview

Major API areas include:

```text
/api/auth
/api/applications
/api/reminders
/api/notifications
/api/dashboard
```

Dashboard APIs currently include functionality for:

```text
Dashboard Summary
Application & Interview Trend Analytics
Conversion Funnel Analytics
```

Full API documentation is available through Swagger while the backend is running.

---

## Engineering Principles

JobTrackr is developed around several engineering principles:

1. Keep controllers thin and business logic in services.
2. Separate API DTOs from database documents.
3. Validate input at API boundaries.
4. Enforce authenticated user-level data isolation.
5. Avoid exposing the existence of another user's resources.
6. Maintain application lifecycle rules in the service layer.
7. Preserve historical state changes for analytics.
8. Use database indexes for important query patterns.
9. Keep secrets and environment-specific configuration out of Git.
10. Run regression tests before committing changes.

---

## Current Development Status

### Completed

- Backend project foundation
- MongoDB integration
- JWT authentication
- Job application CRUD
- Search, filtering, sorting, and pagination
- Application status workflow
- Application status history
- Interview management
- Reminder management
- Reminder scheduler
- Email reminder delivery
- Retry handling
- Notification center
- Dashboard summary
- Upcoming interviews and reminders
- Application & interview trend analytics
- Conversion funnel analytics

### In Progress

- React frontend
- Frontend authentication
- Application management UI
- Dashboard visualization
- Reminder and notification UI

### Planned

- Expanded automated testing
- Code quality tooling
- Dockerization
- CI/CD
- Deployment
- Final documentation and portfolio polish

---

## Project Goal

JobTrackr is designed as a portfolio-grade full-stack application demonstrating practical software engineering skills beyond basic CRUD functionality.

The project focuses on:

- Backend architecture
- Authentication and authorization
- Data isolation
- Business-rule enforcement
- Background scheduling
- Email delivery
- Historical state tracking
- Analytics
- API design
- Full-stack integration
- Testing and deployment

---

## License

This project is currently developed as a portfolio and learning project.