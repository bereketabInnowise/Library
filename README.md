# Library REST API

## Overview
This project implements a RESTful API for managing a library's books, authors, and genres, developed as part of Task 6 for the Spring-based "Library" application. The API replaces a console-based UI with HTTP endpoints, built using Spring Boot, Spring MVC, Spring Data JPA, Liquibase, and PostgreSQL. The project includes Postman collections for testing and comprehensive documentation.

## Task 6 Requirements
- **Postman Setup**: Installed Postman, tested `https://httpbin.org`, and created/exported a collection (`HttpBinCollection.postman_collection.json`).
- **API Design**: Designed REST API endpoints and models per best practices (`LibraryAPI.md`).
- **Spring Data JPA**: Rewrote database queries using JPA repositories (`BookRepository`, etc.).
- **Liquibase**: Added Liquibase for database migrations (`db.changelog-master.yaml`).
- **Spring MVC**: Implemented API endpoints using Spring MVC (`BookController`, etc.).
- **Postman Collection**: Created a Postman collection for the Library API (`LibraryAPICollection.postman_collection.json`).

## Project Structure
- `src/main/java/library/`
  - `controller/`: REST controllers (`BookController`, `AuthorController`, `GenreController`).
  - `service/`: Business logic (`BookService`, `AuthorService`, `GenreService`).
  - `repository/`: JPA repositories (`BookRepository`, `AuthorRepository`, `GenreRepository`).
  - `dto/`: Data Transfer Objects (`BookDTO`, `BookCreateDTO`, etc.).
  - `mapper/`: Entity-DTO conversion (`LibraryMapper`).
  - `exception/`: Global exception handling (`GlobalExceptionHandler`).
  - `model/`: JPA entities (`Book`, `Author`, `Genre`).
  - `config/`: Spring configuration (`WebConfig` for AOP, `MessageSource`).
- `src/main/resources/`
  - `application.properties`: DB, JPA, Liquibase, EHCache settings.
  - `db/changelog/`: Liquibase migrations (`01-create-tables.yaml`, `02-insert-test-data.yaml`).
- `docs/`
  - `LibraryAPI.md`: REST API design.
  - `HttpBinCollection.postman_collection.json`: Postman collection for `httpbin.org`.
  - `LibraryAPICollection.postman_collection.json`: Postman collection for Library API.

## Setup Instructions
1. **Prerequisites**:
   - Java 21
   - Gradle 8.x
   - PostgreSQL (create database `library`)
   - Postman (for API testing)

2. **Clone Repository**:
   ```bash
   git clone https://github.com/bereketabInnowise/Library.git
   cd Library
   ```

3. **Configure Database**:
   - Ensure PostgreSQL is running.
   - Create database:
     ```bash
     psql -U postgres -c "CREATE DATABASE library;"
     ```
   - Create `src/main/resources/application.properties`:
     ```properties
     spring.datasource.username=yourusername
     spring.datasource.password=yourpassword
     ```

4. **Build and Run**:
   ```bash
   ./gradlew clean build
   ./gradlew bootRun
   ```
   - App runs on `http://localhost:8080`.
   - Liquibase creates tables (`books`, `authors`, `genres`, `book_genres`) and inserts test data.

5. **Test API with Postman**:
   - Import `docs/LibraryAPICollection.postman_collection.json` into Postman.
   - Test endpoints (e.g., `GET http://localhost:8080/api/v1/books`).
   - Refer to `docs/LibraryAPI.md` for endpoint details.

## API Endpoints
See `docs/LibraryAPI.md` for full details. Key endpoints:
- **Books**: `GET /api/v1/books`, `POST /api/v1/books`, `PATCH /api/v1/books/{id}`, etc.
- **Authors**: `GET /api/v1/authors`, `POST /api/v1/authors`, etc.
- **Genres**: `GET /api/v1/genres`, `POST /api/v1/genres`, etc.

## Features
- **REST Best Practices**: Versioned URLs (`/api/v1/`), standard HTTP methods, status codes.
- **Pagination**: Supported via `?page=0&size=10`.
- **Error Handling**: Standardized JSON errors (`ErrorResponseDTO`).
- **AOP Logging**: Enabled via `WebConfig` (method execution logging).
- **EHCache**: Second-level cache for JPA queries (`missing_cache_strategy=create`).
- **Liquibase**: Manages DB schema and test data.

## Testing
- **Postman**: Use `LibraryAPICollection.postman_collection.json` to test all endpoints.
- **Database**: Verify data:
  ```bash
  psql -U postgres -d library -c "SELECT * FROM books;"
  ```
- **Build**: Ensure no errors:
  ```bash
  ./gradlew clean build
  ```

## Notes
- Console UI removed, fully REST-based.


## Deliverables
- `docs/LibraryAPI.md`: API design.
- `docs/HttpBinCollection.postman_collection.json`: `httpbin.org` tests.
- `docs/LibraryAPICollection.postman_collection.json`: Library API tests.
- Source code: `rest-task` branch.


## Author
Bereketab: bereketab.shanka@innowise.com questions or feedback welcome!

Have a Good day!