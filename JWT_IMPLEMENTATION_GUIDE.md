# JWT Authentication Implementation - Complete Guide

## What Was Fixed:

1. ✅ **JWT Service** - Updated to use modern JJWT 0.12.x API (fixed `parserBuilder()` error)
2. ✅ **JWT Authentication Filter** - Created to intercept and validate JWT tokens
3. ✅ **Security Config** - Updated with JWT filter and role-based access control
4. ✅ **Auth Controller** - Created with login, signup, and admin signup endpoints
5. ✅ **Admin Controller** - Created with admin-only operations
6. ✅ **User Service** - Added `getUserFromJwt()` method
7. ✅ **Todo Repository** - Added missing `findByUser()` methods
8. ✅ **DTOs Created** - LoginRequest, SignupRequest, AuthResponse

## Admin Role Implementation:

### Two Types of Users:
- **Regular User** - Role: `ROLE_USER` - Can only access `/api/todos/**`
- **Admin User** - Roles: `ROLE_USER`, `ROLE_ADMIN` - Can access both `/api/todos/**` and `/api/admin/**`

---

## How to Run:

```bash
cd /home/user/Downloads/Todo
mvn clean install
mvn spring-boot:run
```

---

## API Testing Guide:

### 1. **Regular User Signup**
```http
POST http://localhost:8080/api/auth/signup
Content-Type: application/json

{
    "username": "john",
    "password": "password123"
}
```

**Response:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huIi...",
    "username": "john",
    "userId": 1,
    "roles": ["ROLE_USER"]
}
```

---

### 2. **Admin User Signup**
```http
POST http://localhost:8080/api/auth/signup/admin?adminSecret=ADMIN_SECRET_KEY_2024
Content-Type: application/json

{
    "username": "admin",
    "password": "admin123"
}
```

**Response:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiI...",
    "username": "admin",
    "userId": 2,
    "roles": ["ROLE_USER", "ROLE_ADMIN"]
}
```

**Note:** Admin signup requires `adminSecret=ADMIN_SECRET_KEY_2024` query parameter

---

### 3. **Login**
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
    "username": "john",
    "password": "password123"
}
```

**Response:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huIi...",
    "username": "john",
    "userId": 1,
    "roles": ["ROLE_USER"]
}
```

---

## User Endpoints (Regular users can access):

### 4. **Create Todo**
```http
POST http://localhost:8080/api/todos
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
Content-Type: application/json

{
    "title": "Learn Spring Boot",
    "description": "Complete JWT implementation",
    "completed": false
}
```

---

### 5. **Get All My Todos**
```http
GET http://localhost:8080/api/todos
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

### 6. **Get Completed Todos**
```http
GET http://localhost:8080/api/todos?completed=true
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

### 7. **Search Todos**
```http
GET http://localhost:8080/api/todos?keyword=spring
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

### 8. **Update Todo**
```http
PUT http://localhost:8080/api/todos/1
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
Content-Type: application/json

{
    "title": "Learn Spring Boot - Updated",
    "description": "Complete JWT implementation with admin role",
    "completed": true
}
```

---

### 9. **Toggle Todo Complete Status**
```http
PATCH http://localhost:8080/api/todos/1/toggle?completed=true
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

### 10. **Delete Todo**
```http
DELETE http://localhost:8080/api/todos/1
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

### 11. **Delete All My Todos**
```http
DELETE http://localhost:8080/api/todos/deleteAll
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

## Admin Only Endpoints:

### 12. **Get All Users** (Admin Only)
```http
GET http://localhost:8080/api/admin/users
Authorization: Bearer <ADMIN_TOKEN>
```

**Response:**
```json
[
    {
        "id": 1,
        "username": "john",
        "roles": ["ROLE_USER"]
    },
    {
        "id": 2,
        "username": "admin",
        "roles": ["ROLE_USER", "ROLE_ADMIN"]
    }
]
```

---

### 13. **Get All Todos (All Users)** (Admin Only)
```http
GET http://localhost:8080/api/admin/todos
Authorization: Bearer <ADMIN_TOKEN>
```

---

### 14. **Get Todos by User ID** (Admin Only)
```http
GET http://localhost:8080/api/admin/users/1/todos
Authorization: Bearer <ADMIN_TOKEN>
```

---

### 15. **Delete Any User** (Admin Only)
```http
DELETE http://localhost:8080/api/admin/users/1
Authorization: Bearer <ADMIN_TOKEN>
```

**Response:**
```json
{
    "message": "User and all their todos deleted successfully"
}
```

---

### 16. **Delete Any Todo** (Admin Only)
```http
DELETE http://localhost:8080/api/admin/todos/1
Authorization: Bearer <ADMIN_TOKEN>
```

**Response:**
```json
{
    "message": "Todo deleted successfully"
}
```

---

### 17. **Get System Statistics** (Admin Only)
```http
GET http://localhost:8080/api/admin/stats
Authorization: Bearer <ADMIN_TOKEN>
```

**Response:**
```json
{
    "totalUsers": 5,
    "totalTodos": 20,
    "completedTodos": 12,
    "pendingTodos": 8
}
```

---

## Testing with Postman/Bruno/Insomnia:

1. **First, signup/login** to get your JWT token
2. **Copy the token** from the response
3. **Add Authorization header** to all protected endpoints:
   - Header: `Authorization`
   - Value: `Bearer <your-token-here>`

---

## Testing with cURL:

### Signup:
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"username":"john","password":"password123"}'
```

### Login:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"john","password":"password123"}'
```

### Create Todo (replace TOKEN with actual token):
```bash
curl -X POST http://localhost:8080/api/todos \
  -H "Authorization: Bearer TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"title":"Learn Spring","description":"Complete Todo App","completed":false}'
```

### Get Todos:
```bash
curl -X GET http://localhost:8080/api/todos \
  -H "Authorization: Bearer TOKEN"
```

---

## Error Responses:

### 403 Forbidden (No token or invalid token):
```json
{
    "timestamp": "2025-12-01T10:42:35.094+00:00",
    "status": 403,
    "error": "Forbidden",
    "path": "/api/todos"
}
```

**Fix:** Make sure you include `Authorization: Bearer <token>` header

---

### 403 Forbidden (Regular user accessing admin endpoint):
```json
{
    "timestamp": "2025-12-01T11:00:00.000+00:00",
    "status": 403,
    "error": "Forbidden",
    "path": "/api/admin/users"
}
```

**Fix:** Only admin users can access `/api/admin/**` endpoints

---

### 401 Unauthorized (Wrong credentials):
```json
"Invalid username or password"
```

---

## Security Configuration Summary:

- **Public Endpoints:** `/api/auth/**` (login, signup)
- **User Endpoints:** `/api/todos/**` (requires `ROLE_USER`)
- **Admin Endpoints:** `/api/admin/**` (requires `ROLE_ADMIN`)
- **CSRF:** Disabled for REST API
- **Session:** Stateless (JWT-based)
- **Token Expiration:** 1 hour (3600000 ms)

---

## Database Schema:

### users table:
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE user_roles (
    user_id BIGINT,
    role VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### todos table:
```sql
CREATE TABLE todos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255),
    description TEXT,
    completed BOOLEAN,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

---

## Important Files Created/Updated:

1. `/src/main/java/com/example/Todo/security/jwtService.java` - JWT token generation/validation
2. `/src/main/java/com/example/Todo/security/JwtAuthenticationFilter.java` - JWT filter
3. `/src/main/java/com/example/Todo/security/SecurityConfig.java` - Security configuration
4. `/src/main/java/com/example/Todo/controller/AuthController.java` - Login/signup endpoints
5. `/src/main/java/com/example/Todo/controller/AdminController.java` - Admin endpoints
6. `/src/main/java/com/example/Todo/service/UserService.java` - User service with JWT helper
7. `/src/main/java/com/example/Todo/dto/LoginRequest.java` - Login DTO
8. `/src/main/java/com/example/Todo/dto/SignupRequest.java` - Signup DTO
9. `/src/main/java/com/example/Todo/dto/AuthResponse.java` - Auth response DTO
10. `/src/main/resources/application.properties` - JWT secret configuration

---

## Next Steps:

1. ✅ Run the application: `mvn spring-boot:run`
2. ✅ Test signup endpoint to create users
3. ✅ Test login endpoint to get JWT token
4. ✅ Test todo CRUD operations with JWT token
5. ✅ Create admin user and test admin endpoints
6. ✅ Verify regular users cannot access admin endpoints

---

## Common Issues & Solutions:

### Issue 1: "Cannot resolve method 'parserBuilder'"
**Fixed:** Updated to use `Jwts.parser()` with JJWT 0.12.x API

### Issue 2: 403 Forbidden on POST requests
**Fixed:** Added JWT authentication and disabled CSRF for REST API

### Issue 3: "Cannot resolve method 'deleteAll'"
**Fixed:** Updated TodoService to use `todoRepo.deleteAll(todoRepo.findByUser(user))`

### Issue 4: User model mismatch
**Fixed:** User entity uses `username`, `password`, `roles` (Set<String>)

### Issue 5: TodoRepository methods not found
**Fixed:** Added `findByUser()` and `findByUserAndTitleContainingIgnoreCase()` methods

---

**Your JWT authentication is now fully implemented with admin role support!** 🎉

