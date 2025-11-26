# Event Tracking System

A full-stack event management application built with Spring Boot and React. This project demonstrates event tracking management with CRUD operations.

## Demo

https://github.com/user-attachments/assets/0bd8f887-04a9-4b5e-b5a8-cd9cf5387d84


## Landing Page
<img width="1736" height="900" alt="Screenshot 2025-11-25 at 5 25 30 PM" src="https://github.com/user-attachments/assets/f010e3f6-25e2-469a-9711-f1634446b2c1" />

## Create new Event
<img width="1736" height="930" alt="Screenshot 2025-11-25 at 5 25 58 PM" src="https://github.com/user-attachments/assets/8558c882-be15-4321-8506-ae4eda7e5314" />

## Edit Event
<img width="1504" height="952" alt="Screenshot 2025-11-25 at 8 53 23 PM" src="https://github.com/user-attachments/assets/cf261159-ad63-4f37-a481-6c433b30e7f4" />

## Delete Event
<img width="1736" height="930" alt="Screenshot 2025-11-25 at 5 27 27 PM" src="https://github.com/user-attachments/assets/e37cf14e-803a-44d8-8d90-efa950df28b6" />

## Quick Start

### Backend Setup
```bash
cd event-tracking-service
./gradlew bootRun
```
Backend runs on `http://localhost:8080`

### Frontend Setup
```bash
cd event-tracking-ui
npm install
npm start
```
Frontend runs on `http://localhost:3000`

## Documentation

- **[Backend Documentation](./event-tracking-service/README.md)** - Spring Boot API details, endpoints, and setup
- **[Frontend Documentation](./event-tracking-ui/README.md)** - React components, features, and configuration

## Features

### Backend
- RESTful API with full CRUD operations
- Spring Data JPA with Hibernate ORM
- H2 in-memory database
- CORS configuration for frontend integration
- Comprehensive unit tests with JUnit 5

### Frontend
- React 18 with hooks
- Real-time event filtering and search
- Automatic event categorization (active/archived)
- Form validation with error messages
- Responsive design with CSS Grid & Flexbox
- Axios for API communication

## Architecture

```
┌─────────────────────────────────────┐
│     React Frontend (port 3000)      │
│     └─ EventDashboard              │
│        ├─ EventForm (CRUD)         │
│        ├─ EventTable (Active)      │
│        └─ EventTable (Archived)    │
├─────────────────────────────────────┤
│     Axios HTTP Client               │
├─────────────────────────────────────┤
│   Spring Boot Backend (port 8080)   │
│   └─ EventController                │
│      └─ EventRepository (JPA)       │
│         └─ H2 Database              │
└─────────────────────────────────────┘
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/events` | Get all events |
| GET | `/api/events/{id}` | Get event by ID |
| POST | `/api/events` | Create event |
| PUT | `/api/events/{id}` | Update event |
| DELETE | `/api/events/{id}` | Delete event |

## Technologies

### Backend
- Java 17
- Spring Boot 4.0.0
- Spring Data JPA
- Hibernate 7.1.8
- H2 Database
- Gradle 9.2.1
- JUnit 5

### Frontend
- React 18.2.0
- Axios 1.6.2
- CSS3
- Node.js 20.9.0
- npm

## Testing

**Backend Tests**
```bash
cd event-tracking-service
./gradlew test
```

## CORS Configuration

The backend allows requests from `http://localhost:3000`. Modify `CorsConfig.java` to add additional origins.

## Development Workflow

1. Start Backend: `cd event-tracking-service && ./gradlew bootRun`
2. Start Frontend**: `cd event-tracking-ui && npm start`
3. Access Application**: Open `http://localhost:3000` in browser
4. Create/Manage Events: Use the UI to perform CRUD operations
5. View Logs: Check console output for errors/debugging
