# Event Tracking UI - Frontend

React application for managing events with real-time categorization into active and archived sections, table filtering, and user interface.

## High-Level Overview

The Event Tracking UI provides an interface for event management. It communicates with the backend REST APIs to perform CRUD operations.

### Architecture Components

```
┌──────────────────────────────────────────────┐
│          React Application                   │
├──────────────────────────────────────────────┤
│   EventDashboard (Main Container)            │
│   ├── EventForm (Create/Update Component)    │
│   ├── EventTable (Active/archived Events)    │
├──────────────────────────────────────────────┤
│   Event Service (API Communication)          │
│   └── Axios HTTP Client                      │
├──────────────────────────────────────────────┤
│   Backend API                                │
│   └── Spring Boot Server (port 8080)         │
└──────────────────────────────────────────────┘
```

## Features

- **Active Events Table**: View and manage ongoing and upcoming events
- **Archived Events Table**: View completed events
- **Create Events**: Add new events with details
- **Edit Events**: Update event information
- **Delete Events**: Remove events from the system
- **Date-based Categorization**: Automatically separates active and archived events
- **Form Validation**: Client-side validation for event data (date and required validation)
- **Real-time Filtering**: Search events by name, description, or location
- **Real-time Updates**: Automatic refresh after CRUD operations

## Technologies Used

React (18.2.0) - UI framework
React (18.2.0) - DOM Rendering
Axios (1.6.1) - HTTP Client
CSS3 - Styling
Node.js (20.9.0) - Runtime environment
npm (11.0+) - Package Manager
React Scripts (5.0.1) - Build and Dev tools

## Component Structure

### 1. EventDashboard (Main Component)
`EventDashboard.js`

The main container component that manages application state and orchestrates all child components.

### 2. EventTable (Display Component)
 `EventTable.js`

Displays events in a table format with filtering option.

### 3. EventForm (Input Component)
 `EventForm.js`

Form for creating and editing events with comprehensive validation.

### 4. Event Service (API Layer)
`eventService.js`

Handles all HTTP communication with the backend API.

**Configuration**:
```javascript
const API_URL = 'http://localhost:8080/api/events';
```

## Setup & Installation

### Prerequisites
- Node.js 20.9.0 or higher
- npm 11.0 or higher

### Installation

**Install dependencies:**
```bash
cd event-tracking-ui
npm install
```

**Start development server:**
```bash
npm start
```

The application will open on `http://localhost:3000`

**Build for production:**
```bash
npm run build
```

## Installation

1. Navigate to the project directory:
```bash
cd event-tracking-ui
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm start
```

The application will open at `http://localhost:3000`

## Configuration

The application connects to the backend API at:
```
http://localhost:8080/api/events
```


### `npm start`
Runs the app in development mode.
Open (http://localhost:3000) to view it in the browser.

### `npm build`
Builds the app for production to the `build` folder.

### `npm test`
Launches the test  