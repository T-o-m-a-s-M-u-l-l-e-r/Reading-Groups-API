# Reading Groups API

A Spring Boot REST API for collaborative reading and annotation. Users can create accounts, create reading groups around shared PDF texts, and retrieve groups and their members. The project is being developed toward supporting passage-level discussions, comments, and questions directly connected to shared reading materials.

The project focuses on building a secure and maintainable backend using Spring Security, JWT authentication with access and refresh tokens, PostgreSQL, file handling and validation, RESTful API design, automated testing, containerization, and continuous integration.

> **Status:** Work in progress. Authentication, reading group creation, PDF upload, and group/member retrieval are implemented. Group membership management and annotations are ongoing.

**Tech Stack:** Java 17 · Spring Boot · Spring Security · JPA/Hibernate · PostgreSQL · JWT · Maven · Docker · Swagger/OpenAPI · JUnit 5 · Mockito · GitHub Actions

---

## Features

### Implemented

* User registration and authentication
* JWT authentication with separate access and refresh tokens
* Access token renewal using refresh tokens
* Logout with refresh token revocation
* BCrypt password hashing
* Stateless Spring Security configuration
* Request validation using Bean Validation
* Reading group creation and retrieval
* Reading group member retrieval
* PDF reading material upload and storage
* PDF content validation using Apache PDFBox
* UUID-based filenames for stored reading materials
* DTO-based API request and response models
* Global exception handling with structured API error responses
* Swagger/OpenAPI documentation
* Unit and controller tests
* Dockerized application and PostgreSQL database
* Continuous integration with GitHub Actions

### In Progress

* Joining and leaving reading groups
* Passage-level comments and questions
* Group permissions and moderation
* Further reading group management

---

## API Documentation

The REST API is documented using Swagger/OpenAPI.

![Swagger UI](screenshots/swagger-ui.png)

When the application is running, the interactive Swagger UI is available at:

`http://localhost:8080/swagger-ui/index.html`

Swagger UI provides the current API contract, including available endpoints, request parameters, request/response models, and authentication requirements.

---

## Authentication

The API uses stateless JWT authentication with separate **access** and **refresh** tokens.

After registration or login, the client receives both tokens. The access token is used to authenticate protected requests, while the longer-lived refresh token can be exchanged for a new access token without requiring the user to log in again.

The authentication flow is:

1. A user registers or logs in with their credentials.
2. The server returns an access token and a refresh token.
3. Protected requests include the access token as a Bearer token.
4. When the access token expires, the refresh token can be used to obtain a new one.
5. On logout, the refresh token is revoked and can no longer be used.

Passwords are hashed using BCrypt and the server does not maintain HTTP sessions.

---

## Database

The project uses PostgreSQL with JPA/Hibernate for persistence.

The database currently contains entities for users, reading groups, categories, and comments, with a many-to-many relationship between users and reading groups.

Hibernate is configured to validate the application entities against the existing database schema rather than generate the schema automatically.

The SQL schema is available at:

`src/main/resources/schema.sql`

---

## File Storage

Reading groups can be created with an uploaded PDF as their shared reading material.

Uploaded files are validated using Apache PDFBox to verify that they contain a valid PDF document rather than relying only on the supplied filename or extension. Valid files are stored under UUID-based filenames to avoid collisions.

By default, uploaded files are stored in:

`./uploads`

When running with Docker Compose, file storage is persisted using a Docker volume.

---

## Running the Project

### Requirements

For Docker:

* Docker with Docker Compose support

For local development:

* Java 17
* Maven, or the included Maven Wrapper
* PostgreSQL

### Docker Compose

The repository includes a Dockerfile and Compose configuration for running the application together with PostgreSQL.

Create an environment file based on `.env.example`:

```env
DB_NAME=test_database
DB_USER=database_user
DB_PASSWORD=password
JWT_SECRET=SecretKey
```

Build and start the application and database:

```bash
docker compose up --build
```

To stop the containers:

```bash
docker compose down
```

### Local Development

For local development, create your local environment file from `.env.local.example`:

```bash
cp .env.local.example .env.local
```

Update `.env.local` with your PostgreSQL credentials and JWT secret:

```env
DB_URL=jdbc:postgresql://localhost:5432/<database_name>
DB_USERNAME=<database_username>
DB_PASSWORD=<database_password>
JWT_SECRET=<jwt_signing_secret>
```

Load the environment variables into your shell:

```bash
source .env.local
```

The PostgreSQL database must contain the schema defined in `src/main/resources/schema.sql`.

Then run the application using the Maven Wrapper:

```bash
./mvnw spring-boot:run
```

## Testing

The project contains automated controller and service tests using JUnit, Mockito, and Spring's testing support.

Run the test suite with:

```bash
./mvnw test
```

On Windows:

```powershell
.\mvnw.cmd test
```

---

## CI/CD

GitHub Actions runs the project's CI workflow on pushes and pull requests.

The workflow runs the Maven test suite before building the Docker image. On pushes to `main`, the resulting image is published to GitHub Container Registry.

This ensures that published Docker images are built from revisions that have passed the automated test suite.

---

## Project Structure

The backend follows a layered structure separating the API, business logic, persistence, security, and data models.

```text
src/main/java/com/muller_tomas/reading_groups/
├── config/         # Spring Security and application configuration
├── controller/     # REST API controllers
├── dto/            # Request and response DTOs
├── exception/      # Application exceptions and global error handling
├── model/          # JPA entities
├── repository/     # Database repositories
├── service/        # Application and business logic
└── token/          # JWT creation, validation and authentication
```

---

## Roadmap

* Join and leave functionality for reading groups
* Passage-level comments and questions attached to reading materials
* Group roles, permissions, and moderation
* Additional reading group management
* Expanded automated test coverage

The long-term goal is to support collaborative reading groups where members can discuss specific parts of a shared text through structured, passage-level annotations.
