# Library Management System

A simple console-based CRUD application for managing books, built with Spring and Jackson CSV Mapper. This project demonstrates Inversion of Control (IoC), Dependency Injection (DI), and the Single Responsibility Principle (SRP) using a lightweight Spring setup without Spring Boot.

## Features
- **CRUD Operations**: Create, read, update, and delete books stored in a CSV file.
- **Console Interface**: User-friendly menu with options:
  1. Display book list
  2. Create new book
  3. Edit book (with partial updates)
  4. Delete book
  0. Save and exit
- **CSV Storage**: Books are stored in `src/main/resources/books.csv` with format: `id,title,author,description`.
- **Spring IoC**: Uses annotation-based configuration for dependency management.

## Technical Stack
- **Java**: 21
- **Gradle**: Build tool with Kotlin DSL (`build.gradle.kts`)
- **Spring**: `spring-context` (6.1.5) for IoC and DI, no Spring Boot
- **Jackson**: `jackson-databind` and `jackson-dataformat-csv` (2.16.1) for CSV handling



## Setup Instructions
### Prerequisites
- Java 21 JDK
- Gradle (or use the included Gradle Wrapper)

### Build and Run
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/yourusername/Library.git
   cd Library
   ```
2. **Build the Project**:
   ```bash
   ./gradlew clean build
   ```
   - Creates `build/libs/Library-1.0-SNAPSHOT.jar`
3. **Run the Application**:
   ```bash
   java -jar build/libs/Library-1.0-SNAPSHOT.jar
   ```
4. **Interact**:
   - Use the console menu to manage books.
   - Changes are saved to `books.csv`.

### Sample CSV (`books.csv`)
```
id,title,author,description
1,"The Catcher in the Rye","J.D. Salinger","A classic novel about teenage rebellion."
2,"My Dog",Pesco,"About Pesco's dog"
3,"Advanced Java",Tom,"Advanced Java Programming"
4,Jhonny,Maria,"Jhonny's day dream"
```

## Spring Concepts Demonstrated
### Inversion of Control (IoC)
- The Spring container (`AnnotationConfigApplicationContext`) manages object creation and dependency injection, inverting control from the application code to the framework.

### Dependency Injection (DI)
- **Constructor DI**: `ConsoleInterface` receives `BookService` via constructor.
- **Setter DI**: `BookRepository` uses a setter for `csvPath`.
- **Field DI**: Previously used in `BookService` (now constructor-based for best practice).

### Context Configuration
- `AppConfig` uses `@Configuration` and `@ComponentScan` to define and discover beans.
- No XML, fully annotation-driven.

## Design Principles
- **Single Responsibility Principle (SRP)**:
  - `Book`: Data model
  - `BookRepository`: CSV operations
  - `BookService`: Business logic
  - `ConsoleInterface`: User interaction
  - `AppConfig`: Dependency wiring

## Author
Bereketab: bereketab.shanka@innowise.com questions or feedback welcome!
