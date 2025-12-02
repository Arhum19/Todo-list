# Quick Start - JWT Todo App

## Start the Application:
```bash
cd /home/user/Downloads/Todo
mvn spring-boot:run
```

## Quick Test Steps:

### Step 1: Create a regular user
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"pass123"}'
```
**Save the token from response!**

### Step 2: Create an admin user
```bash
curl -X POST "http://localhost:8080/api/auth/signup/admin?adminSecret=ADMIN_SECRET_KEY_2024" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```
**Save the admin token from response!**

### Step 3: Create a todo (use your user token)
```bash
curl -X POST http://localhost:8080/api/todos \
  -H "Authorization: Bearer YOUR_USER_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{"title":"Test Todo","description":"My first todo","completed":false}'
```

### Step 4: Get all your todos
```bash
curl -X GET http://localhost:8080/api/todos \
  -H "Authorization: Bearer YOUR_USER_TOKEN_HERE"
```

### Step 5: Admin - View all users (use admin token)
```bash
curl -X GET http://localhost:8080/api/admin/users \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN_HERE"
```

### Step 6: Admin - View statistics
```bash
curl -X GET http://localhost:8080/api/admin/stats \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN_HERE"
```

---

## All Fixed Issues:

✅ **Jwts.parserBuilder() error** - Fixed (using Jwts.parser() now)  
✅ **403 Forbidden on /api/todos** - Fixed (JWT authentication working)  
✅ **deleteAll() error** - Fixed (TodoService updated)  
✅ **User model mismatch** - Fixed (using username/password/roles)  
✅ **TodoRepository methods** - Fixed (all findByUser methods added)  
✅ **getUserFromJwt() missing** - Fixed (UserService updated)  
✅ **Admin role** - Implemented (ROLE_ADMIN added)  
✅ **Authentication** - Fully working with JWT  

---

## Key Endpoints:

### Public (No auth needed):
- POST `/api/auth/signup` - Create user
- POST `/api/auth/signup/admin?adminSecret=ADMIN_SECRET_KEY_2024` - Create admin
- POST `/api/auth/login` - Login

### User Endpoints (Need JWT token):
- GET `/api/todos` - Get my todos
- POST `/api/todos` - Create todo
- PUT `/api/todos/{id}` - Update todo
- DELETE `/api/todos/{id}` - Delete todo
- DELETE `/api/todos/deleteAll` - Delete all my todos

### Admin Endpoints (Need admin JWT token):
- GET `/api/admin/users` - Get all users
- GET `/api/admin/todos` - Get all todos
- GET `/api/admin/users/{userId}/todos` - Get user's todos
- GET `/api/admin/stats` - Get statistics
- DELETE `/api/admin/users/{userId}` - Delete user
- DELETE `/api/admin/todos/{todoId}` - Delete any todo

---

## Remember:
- Regular users have `ROLE_USER` - can access `/api/todos/**`
- Admin users have `ROLE_USER` + `ROLE_ADMIN` - can access both `/api/todos/**` and `/api/admin/**`
- Always include `Authorization: Bearer <token>` header for protected endpoints
- Tokens expire after 1 hour

**Everything is ready to go! 🚀**

