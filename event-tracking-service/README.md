# Event Tracking Service - Backend

Spring Boot REST API for managing events with full CRUD operations.

## High-Level Overview

The Event Tracking Service is a backend application built with Spring Boot that provides RESTful APIs for managing events. It handles:

- Event Management Create, read, update, and delete events
- Data Persistence: Stores events in H2 database with automatic schema management
- CORS Support: Enables cross-origin requests from the React frontend
- RESTful API: Standard HTTP methods for all operations

### Architecture Components

```
┌─────────────────────────────────────────┐
│        Spring Boot Application          │
├─────────────────────────────────────────┤
│   REST Controller (EventController)     │                                     
│   ↓                                     │
│   Repository Layer (EventRepository)    │
│   ↓                                     │
│   Entity Layer (Event.java)             │
├─────────────────────────────────────────┤
│   H2 Database (In-Memory)               │
└─────────────────────────────────────────┘
```

## Technologies Used
Java (17) - Language
Spring Boot (4.0.0) - Web Framework
Spring Data JPA (4.0.4) - ORM & Data Access
Hibernate (7.1.8) - ORM Implementation
H2 Database (2.4.240) - In-Memory Database
Gradle (9.2.1) - Build Tool
JUnit 5 - Unit Test framework
Jackson (2.15+) - JSON Serialization

## Data Models

### Event Entity

The core data model representing an event in the system.

```java
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    private String name;              
    private String description;       
    private LocalDateTime startDate; 
    private LocalDateTime endDate;    
    private String location;          
    private Integer minAttendees;     // Minimum required attendees
    private Integer maxAttendees;     // Maximum required attendees
    private String locationNotes;     // Specific location details
    private String preparationNotes;  // Notes for preparation
}
```


## API Endpoints

All endpoints are prefixed with `/api/events`

## Setup & Installation

### Prerequisites
- Java 17 or higher
- Gradle 9.2.1 or higher

### Build & Run

**Build the project:**
```bash
cd event-tracking-service
./gradlew clean build
```

**Run the application:**
```bash
./gradlew bootRun
```

The server will start on `http://localhost:8080`

**Run tests:**
```bash
./gradlew test
```

## Testing

The project includes unit tests covering:
- Get all events (success and empty list cases)
- Get event by ID (found and not found cases)
- Create event (success and validation failure cases)
- Update event (success and not found cases)
- Delete event (success and not found cases)

**Run tests:**
```bash
./gradlew test
```