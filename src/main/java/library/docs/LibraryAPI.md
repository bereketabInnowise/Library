# Library REST API Design

## Overview
REST API for managing books, authors, and genres in the Library app, adhering to REST best practices.

## Base URL
`/api/v1/`

## Endpoints

### Books
- **GET /api/v1/books**
  - **Description**: List all books (paginated).
  - **Query Params**:
    - `page` (int, default: 0)
    - `size` (int, default: 10)
    - `authorId` (int, optional): Filter by author.
  - **Response**: `200 OK`
    ```json
    [
      {
        "id": 1,
        "title": "Dune",
        "description": "A sci-fi epic",
        "author": {"id": 1, "name": "Frank Herbert"},
        "genres": [{"id": 1, "name": "Sci-Fi"}]
      }
    ]
    ```
- **GET /api/v1/books/{id}**
  - **Description**: Get book by ID.
  - **Path Param**: `id` (int)
  - **Response**: `200 OK` (BookDTO), `404 Not Found`
- **POST /api/v1/books**
  - **Description**: Create a book.
  - **Body**:
    ```json
    {
      "title": "Dune",
      "description": "A sci-fi epic",
      "authorId": 1,
      "genreIds": [1]
    }
    ```
  - **Response**: `201 Created` (BookDTO), `400 Bad Request`
- **PUT /api/v1/books/{id}**
  - **Description**: Update a book.
  - **Path Param**: `id` (int)
  - **Body**: Same as POST
  - **Response**: `200 OK` (BookDTO), `404 Not Found`
- **PATCH /api/v1/books/{id}**
  - **Description**: Partial update (e.g., title).
  - **Path Param**: `id` (int)
  - **Body**:
    ```json
    {"title": "Dune Messiah"}
    ```
  - **Response**: `200 OK` (BookDTO), `404 Not Found`
- **DELETE /api/v1/books/{id}**
  - **Description**: Delete a book.
  - **Path Param**: `id` (int)
  - **Response**: `204 No Content`, `404 Not Found`
- **GET /api/v1/authors/{authorId}/books**
  - **Description**: List books by author (paginated).
  - **Path Param**: `authorId` (int)
  - **Query Params**: `page`, `size`
  - **Response**: `200 OK` (List<BookDTO>)

### Authors
- **GET /api/v1/authors**
  - **Description**: List all authors (paginated).
  - **Query Params**: `page`, `size`
  - **Response**: `200 OK` (List<AuthorDTO>)
- **GET /api/v1/authors/{id}**
  - **Description**: Get author by ID.
  - **Path Param**: `id` (int)
  - **Response**: `200 OK` (AuthorDTO), `404 Not Found`
- **POST /api/v1/authors**
  - **Description**: Create an author.
  - **Body**:
    ```json
    {"name": "Frank Herbert"}
    ```
  - **Response**: `201 Created` (AuthorDTO)
- **PUT /api/v1/authors/{id}**
  - **Description**: Update an author.
  - **Path Param**: `id` (int)
  - **Body**: Same as POST
  - **Response**: `200 OK` (AuthorDTO), `404 Not Found`
- **DELETE /api/v1/authors/{id}**
  - **Description**: Delete an author.
  - **Path Param**: `id` (int)
  - **Response**: `204 No Content`, `404 Not Found`

### Genres
- **GET /api/v1/genres**
  - **Description**: List all genres (paginated).
  - **Query Params**: `page`, `size`
  - **Response**: `200 OK` (List<GenreDTO>)
- **GET /api/v1/genres/{id}**
  - **Description**: Get genre by ID.
  - **Path Param**: `id` (int)
  - **Response**: `200 OK` (GenreDTO), `404 Not Found`
- **POST /api/v1/genres**
  - **Description**: Create a genre.
  - **Body**:
    ```json
    {"name": "Sci-Fi"}
    ```
  - **Response**: `201 Created` (GenreDTO)
- **PUT /api/v1/genres/{id}**
  - **Description**: Update a genre.
  - **Path Param**: `id` (int)
  - **Body**: Same as POST
  - **Response**: `200 OK` (GenreDTO), `404 Not Found`
- **DELETE /api/v1/genres/{id}**
  - **Description**: Delete a genre.
  - **Path Param**: `id` (int)
  - **Response**: `204 No Content`, `404 Not Found`

## Models
- **BookDTO**:
  ```json
  {
    "id": 1,
    "title": "Dune",
    "description": "A sci-fi epic",
    "author": {"id": 1, "name": "Frank Herbert"},
    "genres": [{"id": 1, "name": "Sci-Fi"}]
  }
  ```
- **AuthorDTO**:
  ```json
  {"id": 1, "name": "Frank Herbert"}
  ```
- **GenreDTO**:
  ```json
  {"id": 1, "name": "Sci-Fi"}
  ```
- **Error**:
  ```json
  {
    "error": "Book not found",
    "status": 404,
    "timestamp": "2025-06-01T00:05:00Z"
  }
  ```

## Best Practices
- Versioned URLs (`/api/v1/`).
- Resource-based nouns (`books`, `authors`).
- Standard HTTP methods and status codes.
- Pagination for lists (`page`, `size`).
- JSON request/response bodies.
- Clear error responses.

## Notes
- Implemented in Spring MVC (next steps).
- Tested via Postman collection.
- Supports Spring Data JPA and Liquibase.