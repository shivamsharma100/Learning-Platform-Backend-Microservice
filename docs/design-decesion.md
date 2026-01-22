
---

# Design Decisions

## 1. Architecture

The application follows a **Layered Architecture**:

* **Controller Layer** (`com.example.course.mycourse`)
  Handles HTTP requests, validation, and delegates to services.
* **Service Layer** (`com.example.course.service`)
  Contains business logic and enforces rules.
* **Repository Layer** (`com.example.course.repositories`)
  Abstracts database operations using Spring Data JPA.
* **Entity Layer** (`com.example.course.entities`)
  Represents domain models: `Course`, `Enrollment`, `Lesson`.

**Benefits:** separation of concerns, maintainability, testability, and scalability.

---

## 2. Design Patterns

**Repository Pattern**

* Abstracts persistence logic via Spring Data JPA repositories (`CourseRepository`, `EnrollmentRepositories`, `LessonRepository`).

**Dependency Injection Pattern**

* Constructor-based DI using Spring’s IoC container (`@RequiredArgsConstructor`, `@Service`) to ensure loose coupling and easy testing.

---

## 3. SOLID Principles

* **SRP:** Controllers handle requests; services handle business logic; entities handle data.
* **OCP:** Services can be extended without modifying controllers.
* **LSP:** Repository interfaces can be substituted without affecting services.
* **ISP:** Each service focuses on a single business domain.
* **DIP:** Controllers depend on service abstractions, not concrete repositories.

---

## 4. Conclusion

The design ensures **clean separation of concerns**, leverages **Spring Boot best practices**, and follows **SOLID principles**.
Patterns and layering enhance maintainability, flexibility, and scalability of the microservice.

---
