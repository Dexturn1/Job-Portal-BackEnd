# Job Portal Backend

**Live API:** [dex-jobportal.ap-south-1.elasticbeanstalk.com](http://dex-jobportal.ap-south-1.elasticbeanstalk.com/)

**Interactive API documentation (Swagger UI):** [Open Swagger UI](http://dex-jobportal.ap-south-1.elasticbeanstalk.com/swagger-ui/index.html)

REST API for a job portal, built with Spring Boot. It supports job seekers, employers, and administrators, with role-based access to profiles, jobs, applications, companies, and contact requests.

## Features

- JWT authentication and role-based authorization for `JOB_SEEKER`, `EMPLOYER`, and `ADMIN`
- Job seeker profiles, resume and profile-picture access, saved jobs, and applications
- Employer job listings and applicant/application-status management
- Public company browsing and administrator company management
- Public contact submissions and administrator review
- MySQL persistence with Spring Data JPA and Hibernate
- Caffeine caching, Spring Boot Actuator, and OpenTelemetry instrumentation

## Technology

| Area | Technologies |
|---|---|
| Language and framework | Java 25, Spring Boot 4.1.1 |
| API | Spring MVC, Spring Security, Spring Validation, Springdoc OpenAPI |
| Persistence | MySQL, Spring Data JPA, Hibernate |
| Other | JWT, Caffeine, Actuator, OpenTelemetry, Maven Wrapper |

## API at a glance

Application endpoints are grouped under the `/api` prefix. Use the live Swagger UI above to explore request schemas and the complete endpoint list.

| Workflow | Example endpoints |
|---|---|
| Authentication | `POST /api/auth/register/public`, `POST /api/auth/login/public` |
| Public browsing | `GET /api/companies/public` |
| Contact | `POST /api/contacts/public` |
| Job seeker | `/api/users/profile/jobseeker`, `/api/users/saved-jobs/jobseeker`, `/api/users/job-applications/jobseeker` |
| Employer | `/api/jobs/employer`, `/api/jobs/applications/{jobId}/employer` |
| Administration | `/api/companies/admin`, `/api/contacts/admin`, `/api/users/search/admin` |

Most non-public routes require a JWT. After signing in, send it in the request header:

```http
Authorization: Bearer <token>
```

The login endpoint accepts a JSON request like:

```json
{
  "username": "user@example.com",
  "password": "your-password"
}
```

## Run locally

### Prerequisites

- JDK 25
- Docker with Docker Compose

### Start the application

Clone the repository and start the application from its root:

```bash
git clone https://github.com/Dexturn1/Job-Portal-BackEnd.git
cd Job-Portal-BackEnd
./mvnw spring-boot:run
```

Spring Boot's Docker Compose integration starts the MySQL and Grafana LGTM services defined in `compose.yaml` during local development. MySQL is published on port `3307`; Grafana is available on port `3000`.

Local URLs:

- API base: `http://localhost:8080/api`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- Actuator: `http://localhost:9090/jobportal/actuator`
- Grafana: `http://localhost:3000`

To build and test:

```bash
./mvnw test
./mvnw package
```

The packaged application is `target/jobportal-aws-deployment.jar`.

## Configuration

Default application configuration is in `src/main/resources/application.properties`. Database and frontend CORS settings can be overridden with environment variables:

| Variable | Default | Purpose |
|---|---|---|
| `DATABASE_HOST` | `localhost` | MySQL host |
| `DATABASE_PORT` | `3307` | MySQL port |
| `DATABASE_NAME` | `jobportal` | Database name |
| `DATABASE_USERNAME` | `root` | Database username |
| `DATABASE_PASSWORD` | — | Database password |
| `APP_CORS_ALLOWED_ORIGINS` | `http://localhost:5173` | Allowed frontend origins |

For example:

```bash
export DATABASE_HOST=localhost
export DATABASE_PORT=3307
export DATABASE_NAME=jobportal
export DATABASE_USERNAME=root
export DATABASE_PASSWORD=your-local-password
./mvnw spring-boot:run
```

Do not commit real credentials. Production and QA overrides are maintained in `application-prod.properties` and `application-qa.properties`.

## Project layout

```text
src/main/java/com/dexturn/jobportal/
  auth/          Authentication endpoints
  company/       Company APIs and services
  Contact/       Contact request APIs and services
  job/           Job APIs and services
  user/          User and profile APIs and services
  security/      JWT authentication and authorization
  entity/        JPA entities
  repository/    Spring Data repositories
  dto/           API request and response models
  config/        Application configuration
src/main/resources/
  application*.properties
  sql/           Database schema and seed data
compose.yaml     Local MySQL and Grafana LGTM services
```

## Observability

The application exposes Spring Boot Actuator endpoints on port `9090` under `/jobportal/actuator`. Local OpenTelemetry metrics, traces, and logs are configured to export to the Grafana LGTM service in `compose.yaml`. Caffeine caches are configured for jobs, companies, and roles.

## License

No license is currently declared in this repository. Check with the repository owner before redistributing or using the project commercially.
