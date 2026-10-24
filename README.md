# Job Portal Backend

A Spring Boot-based backend for a job portal application that supports job seekers, employers, and administrators. The system manages company records, job listings, candidate profiles, job applications, saved jobs, contact submissions, and role-based authentication using JWT.

This project is built with Spring Boot 4, Java 25, MySQL, Spring Data JPA, Spring Security, caching, OpenTelemetry, logging, and Docker-based local infrastructure.

## Overview

The backend exposes REST APIs that power a job portal platform with the following core workflows:

- Job seekers can register, log in, create a profile, upload a resume and profile picture, save jobs, and apply for jobs.
- Employers can create and manage jobs, update application statuses, and review candidates who applied.
- Admins can manage users, review contact submissions, and manage companies.
- Public endpoints support company browsing, authentication, and contact inquiries.
- Application monitoring, health checks, and metric export are included for operational visibility.

## Key Features

- JWT-based authentication and role-based authorization
- User registration and login with password encoding
- Role separation for ADMIN, EMPLOYER, and JOB_SEEKER
- Employer-side job management and status updates
- Candidate profile management with resume and profile picture upload
- Saved jobs and job application tracking
- Company catalog and admin company management
- Contact form support with admin review workflows
- Caching with Caffeine for frequently accessed data
- Actuator-based health and metrics endpoint exposure
- OpenTelemetry tracing/log export
- Docker Compose setup for MySQL and Grafana LGTM observability stack

## Tech Stack

- Java 25
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA
- Spring Security
- Hibernate / JPA
- MySQL
- JWT (jjwt)
- Caffeine Cache
- Spring Validation
- OpenAPI / Swagger UI
- Actuator
- OpenTelemetry
- Lombok
- Maven Wrapper
- Docker Compose

## Project Architecture

The application follows a layered structure:

- Controllers: REST API endpoints
- Services: business logic and orchestration
- Repositories: database access via Spring Data JPA
- Entities: JPA entities and DB mappings
- DTOs: request and response payloads
- Security: JWT validation and access control
- Config: CORS, path prefixing, metrics, caching, and app settings
- Aspects: auditing, logging, and performance tracing

## Project Structure

```text
src/
  main/
    java/
      com/dexturn/jobportal/
        auth/
        company/
        client/
        Contact/
        config/
        constanst/
        dto/
        entity/
        exception/
        job/
        otel/
        repository/
        security/
        user/
        util/
        cache/
        audit/
        aspacts/
        JobportalApplication.java
    resources/
      application.properties
      application-prod.properties
      application-qa.properties
      jwt.properties
      logback-spring.xml
      sql/
        schema.sql
        data.sql
  test/
    java/
      com/dexturn/jobportal/
        JobportalApplicationTests.java
compose.yaml
pom.xml
mvnw
mvnw.cmd
HELP.md
logs/
```

## Main Modules

### Authentication and Authorization

- `auth/AuthController.java`
- `security/JobPortalSecurityConfig.java`
- `security/filter/JwtTokenValidatorFilter.java`
- `security/util/JwtUtil.java`
- `security/PathsConfig.java`

This module handles:

- user login and registration
- JWT generation and validation
- CSRF token endpoint support
- path-based role restrictions for public, admin, employer, and job-seeker APIs

### User Management

- `user/controller/UserController.java`
- `user/service/impl/UserServiceImpl.java`

User operations include:

- profile creation and update
- profile picture and resume retrieval
- saved job management
- job applications and withdrawals
- admin user search and employer role assignment

### Company and Job Modules

- `company/controller/CompanyController.java`
- `job/controller/JobController.java`

These handle:

- public company listing
- admin company creation, update, and deletion
- employer job creation and listing
- application review and job status updates

### Contact Module

- `Contact/controller/ContactController.java`

Supports:

- public contact submissions
- admin inbox access
- pagination and sorting of contact messages
- status updates for contact requests

### Shared Components

- `entity/`: model classes such as `JobPortalUser`, `Company`, `Job`, `Profile`, `Role`, `JobApplication`
- `repository/`: Spring Data repositories for each entity
- `dto/`: request and response objects for API payloads
- `exception/`: exception handling and validation support
- `aspacts/`: logging, auditing, and performance monitoring
- `cache/`: Caffeine cache configuration
- `otel/`: OpenTelemetry setup

## Database Design

The application uses MySQL with JPA entities mapped to the following major tables:

- `users`
- `roles`
- `companies`
- `jobs`
- `job_applications`
- `saved_jobs`
- `profiles`
- `contacts`

The schema is prepared using SQL files in:

- `src/main/resources/sql/schema.sql`
- `src/main/resources/sql/data.sql`

The project also includes automatic schema updates through Hibernate with:

```properties
spring.jpa.hibernate.ddl-auto=update
```

## Security Model

Security is configured in `JobPortalSecurityConfig` and uses the following patterns:

- public endpoints are publicly accessible
- secured paths require authentication
- admin-only roles are enforced for admin endpoints
- employer-only routes are restricted to employers
- job seeker routes are restricted to candidates
- JWT tokens are validated before processing requests
- CORS is enabled for the frontend origin configured in `application.properties`

The current CORS configuration is:

```properties
app.cors.allowed-origins=http://localhost:5173
app.cors.allowed-methods=*
app.cors.allowed-headers=*
app.cors.allow-credentials=true
```

## Authentication Flow

1. Client sends login credentials to `POST /api/auth/login/public`
2. Server authenticates the user via `AuthenticationManager`
3. A JWT is generated and returned to the client
4. The frontend sends the JWT in the Authorization header for secured routes
5. `JwtTokenValidatorFilter` validates tokens before request processing

A typical login request body looks like:

```json
{
  "username": "user@example.com",
  "password": "your-password"
}
```

A typical registration request:

```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "StrongPassword123",
  "mobileNumber": "+1234567890"
}
```

## Demo / Registered Accounts

The following account is currently known to be registered in the application:

| Role | Email | Password | Notes |
|------|-------|----------|-------|
| Employer | `elon@gmail.com` | `jobPortal@2003` | Access to employer-only routes such as job creation, status updates, and application review |
| Job Seeker | `john@gamail.com` | `jobPortal@2003` | Access to job seeker routes such as profile management, saved jobs, and applications |
| Admin | `admin@gmail.com` | `jobPortal@2003` | Access to admin-only routes such as user and company management |

> Add more demo/test credentials here as they are created for the project. For security reasons, avoid storing production or personal passwords in the repository unless they are explicitly intended for test/demo use.

## API Overview

The application uses a global `/api` prefix via `WebConfig` and supports versioned endpoints (default version `1.0`).

### Public Endpoints

- `POST /api/auth/login/public`
- `POST /api/auth/register/public`
- `GET /api/companies/public`
- `POST /api/contacts/public`
- `GET /api/csrf-token/public`
- `GET /api/logging/public`

### Job Seeker Endpoints

- `PUT /api/users/profile/jobseeker`
- `GET /api/users/profile/jobseeker`
- `GET /api/users/profile/picture/jobseeker`
- `GET /api/users/profile/resume/jobseeker`
- `POST /api/users/saved-jobs/{jobId}/jobseeker`
- `GET /api/users/saved-jobs/jobseeker`
- `POST /api/users/job-applications/jobseeker`
- `GET /api/users/job-applications/jobseeker`

### Employer Endpoints

- `GET /api/jobs/employer`
- `POST /api/jobs/employer`
- `PATCH /api/jobs/{jobId}/status/employer`
- `GET /api/jobs/applications/{jobId}/employer`
- `PATCH /api/jobs/applications/employer`

### Admin Endpoints

- `GET /api/companies/admin`
- `POST /api/companies/admin`
- `PUT /api/companies/{id}/admin`
- `DELETE /api/companies/{id}/admin`
- `GET /api/contacts/admin`
- `GET /api/contacts/sort/admin`
- `GET /api/contacts/page/admin`
- `PATCH /api/contacts/{id}/status/admin`
- `GET /api/users/search/admin`
- `PATCH /api/users/{userId}/role/employer/admin`

## Deployment and Live URL

The backend is deployed in AWS Elastic Beanstalk and is available at:

```text
http://dex-jobportal.ap-south-1.elasticbeanstalk.com/
```

This base URL is used for live API access in production. For example, the API endpoints are typically called as:

```text
http://dex-jobportal.ap-south-1.elasticbeanstalk.com/api/auth/login/public
http://dex-jobportal.ap-south-1.elasticbeanstalk.com/api/companies/public
```

## Local Development Setup

### Prerequisites

- JDK 25
- Maven 3.9+
- Docker Desktop or Docker Engine
- Git

### 1. Clone the Repository

```bash
git clone https://github.com/Dexturn1/Job-Portal-BackEnd.git
cd Job-Portal-BackEnd
```

### 2. Start Local Infrastructure

This project includes a Docker Compose file that starts:

- MySQL database on port `3307`
- Grafana LGTM observability stack on ports `3000`, `4317`, and `4318`

```bash
docker compose up -d
```

### 3. Run the Application

Using Maven Wrapper:

```bash
./mvnw clean install
./mvnw spring-boot:run
```

Or run directly with the packaged jar:

```bash
./mvnw package
java -jar target/jobportal-aws-deployment.jar
```

### 4. Check Application Status

The app runs on:

- Application: `http://localhost:8080`
- Actuator: `http://localhost:9090/jobportal/actuator`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- Grafana: `http://localhost:3000`

## Configuration

The application settings live in `src/main/resources/application.properties`.

Key configuration values:

```properties
spring.application.name=jobportal
spring.datasource.url=jdbc:mysql://${DATABASE_HOST:localhost}:${DATABASE_PORT:3307}/${DATABASE_NAME:jobportal}
spring.datasource.username=${DATABASE_USERNAME:root}
spring.datasource.password=${DATABASE_PASSWORD:root}

management.server.port=9090
management.endpoints.web.base-path=/jobportal/actuator

app.cors.allowed-origins=http://localhost:5173
```

### Environment Variables

You can override values with environment variables:

- `DATABASE_HOST`
- `DATABASE_PORT`
- `DATABASE_NAME`
- `DATABASE_USERNAME`
- `DATABASE_PASSWORD`
- `SHOW_SQL`
- `LOG_LEVEL`
- `HIBERNATE_FORMAT_SQL`

Example:

```bash
export DATABASE_HOST=localhost
export DATABASE_PORT=3307
export DATABASE_NAME=jobportal
export DATABASE_USERNAME=root
export DATABASE_PASSWORD=root
./mvnw spring-boot:run
```

## Caching

The application is configured with Caffeine caching via the `cache` package and properties such as:

```properties
cache.jobs.ttl-minutes=10
cache.jobs.max-size=5000
cache.companies.ttl-minutes=10
cache.companies.max-size=500
cache.roles.ttl-days=1
cache.roles.max-size=100
```

This helps reduce repeated database access for commonly used reference data such as jobs and companies.

## Observability and Monitoring

The project includes:

- Spring Boot Actuator endpoints
- OTLP metrics export to local collector
- tracing and logs exported to OpenTelemetry backend
- file-based logging to `logs/jobportal.log`
- Docker Compose stack for Grafana LGTM

Observability configuration examples:

```properties
management.otlp.metrics.export.url="http://localhost:4318/v1/metrics"
management.opentelemetry.tracing.export.otlp.endpoint="http://localhost:4318/v1/traces"
management.opentelemetry.logging.export.otlp.endpoint="http://localhost:4318/v1/logs"
```

## Logging and Auditing

The application includes aspects for:

- request/response logging
- performance monitoring
- login audit tracking
- registration validation checks
- exception auditing

Files under `aspacts/` handle many of these concerns.

## Testing

The repository includes a basic Spring Boot smoke test:

- `src/test/java/com/dexturn/jobportal/JobportalApplicationTests.java`

To run tests:

```bash
./mvnw test
```

## Build Artifacts

The Maven build uses a custom final jar name:

```xml
<finalName>jobportal-aws-deployment</finalName>
```

Generated artifact:

```text
target/jobportal-aws-deployment.jar
```

## Notes

- The project currently targets Java 25, which is newer than many standard local setups. Ensure your environment is configured for the required JDK version.
- API routes are intentionally grouped under `/api` and secured by role-aware HTTP security configuration.
- The app is designed for a full-stack job portal workflow with both job seeker and employer functionality.

## License

This project does not currently declare a license in `pom.xml`; check with the repository owner before using it in production or redistributing it commercially.

## Contributing

This is a backend service repository for a job portal. Contributions are typically made by updating the API layer, services, entities, DTOs, security rules, or deployment configuration.

## Summary

This project is a modern Spring Boot job portal backend with strong API separation, role-based access control, MySQL persistence, document-style user profiles, employer workflow support, and observability tooling. It is suitable for powering a job board platform with both user-facing and admin operations.
