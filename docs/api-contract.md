

````md
# API Contract – Course Management System

## 1. Overview

This document defines the API contract for the Course Management System.  
The system exposes APIs for managing **Courses**, **Enrollments**, and **Lessons**.

All APIs are secured using **JWT-based authentication** with **role-based authorization**.

---

## 2. Base URL

```text
/api
````

---

## 3. Authentication & Authorization

### Authentication Type

JWT Bearer Token

### Header Format

```text
Authorization: Bearer <jwt_token>
```

### JWT Contains

* username
* password (encoded)
* role (`ADMIN`, `INSTRUCTOR`, `LEARNER`)

---

## 4. Security Rules (Role-Based Access)

| Role       | Access                         |
| ---------- | ------------------------------ |
| ADMIN      | Full access                    |
| INSTRUCTOR | Course & Lesson management     |
| LEARNER    | Read courses, view enrollments |

---

## 5. Data Models

### 5.1 Course

```json
{
  "id": 1,
  "title": "Java Basics",
  "description": "Intro to Java",
  "level": "Beginner",
  "duration": 40,
  "status": "Active"
}
```

---

### 5.2 Enrollment Request

```json
{
  "enrollments": [
    {
      "learnerId": "L1001",
      "status": "ACTIVE",
      "enrolledAt": "2025-01-01T10:00:00Z"
    }
  ]
}
```

**Rules:**

* Maximum **2 enrollments** per request
* `learnerId`, `status`, and `enrolledAt` are mandatory

---

### 5.3 Lesson Request

```json
{
  "lessons": [
    {
      "title": "Introduction",
      "contentUrl": "http://video-url",
      "orderNo": "1"
    }
  ]
}
```

---

## 6. Course APIs

### 6.1 Create Courses (Bulk)

**Endpoint:**

```
POST /api/courses
```

**Roles Allowed:** ADMIN, INSTRUCTOR

**Request Body:**

```json
[
  {
    "title": "Spring Boot",
    "description": "Spring Boot fundamentals",
    "level": "Intermediate",
    "duration": 30,
    "status": "Active"
  }
]
```

---

### 6.2 Get Course by ID

**Endpoint:**

```
GET /api/courses/course/{courseId}
```

**Roles Allowed:** LEARNER, INSTRUCTOR, ADMIN

---

### 6.3 Get All Courses

**Endpoint:**

```
GET /api/courses
```

**Roles Allowed:** LEARNER, INSTRUCTOR, ADMIN

---

### 6.4 Update Course Description

**Endpoint:**

```
PUT /api/courses/{courseId}?description={description}
```

**Roles Allowed:** INSTRUCTOR, ADMIN

---

## 7. Enrollment APIs

### 7.1 Add Enrollments to Course

**Endpoint:**

```
POST /api/enrollment/courses/{courseId}/enrollments
```

**Roles Allowed:** ADMIN

**Request Body:**

```json
{
  "enrollments": [
    {
      "learnerId": "L1001",
      "status": "ACTIVE",
      "enrolledAt": "2025-01-01T10:00:00Z"
    }
  ]
}
```

---

### 7.2 Get Enrollment by Course & Learner

**Endpoint:**

```
GET /api/enrollment/courses/{courseId}/enrollments?learnerID={learnerId}
```

**Roles Allowed:** LEARNER, ADMIN

---

## 8. Lesson APIs

### 8.1 Add Lessons to Course

**Endpoint:**

```
POST /api/lesson/courses/{courseId}/lessons
```

**Roles Allowed:** INSTRUCTOR

**Request Body:**

```json
{
  "lessons": [
    {
      "title": "Lesson 1",
      "contentUrl": "http://video-url",
      "orderNo": "1"
    }
  ]
}
```

**Response:**

```json
true
```

---

### 8.2 Get Lessons for Course

**Endpoint:**

```
GET /api/lesson/courses/{courseId}/lessons
```

**Roles Allowed:** ADMIN, INSTRUCTOR

---

## 9. HTTP Status Codes

| Code | Meaning               |
| ---- | --------------------- |
| 200  | OK                    |
| 400  | Bad Request           |
| 401  | Unauthorized          |
| 403  | Forbidden             |
| 404  | Not Found             |
| 500  | Internal Server Error |

---

## 10. Validation Rules

* Required fields must not be null or empty
* Maximum 2 enrollments per request
* JWT token is required for all APIs


