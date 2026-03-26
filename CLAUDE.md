# 📘 PaperStockIndia — Backend Context (Spring Boot)

## 🧱 System Overview
PaperStockIndia is a full-stack stock paper trading platform.

Architecture:
- Frontend: Angular (deployed on Netlify)
- Backend: Spring Boot (REST API)
- Database: MongoDB
- Cache Layer (Planned): Redis

Design Goal:
- Scalable, stateless backend
- Clean separation of concerns (Controller → Service → Repository)

---

## 📂 Repositories

Frontend:
https://github.com/kiranakella/paperStock
Branch: frontEnd

Backend:
https://github.com/kiranakella/paperStock-backend
Branch: main

---

## ⚙️ Backend Tech Stack

- Java: 21 (LTS)
- Spring Boot: 3.x
- Build Tool: Maven

### Core Dependencies
- spring-boot-starter-web
- spring-boot-starter-data-mongodb
- lombok
- spring-boot-devtools

---

## 📁 Project Structure
com.paperstock.backend
│
├── controller # REST endpoints
├── service # Business logic
├── repository # MongoDB repositories
├── model # Domain models (entities)
├── dto # Data Transfer Objects (future)
├── config # Configuration (CORS, Redis, Security)
└── BackendApplication.java


---

## 🧠 Backend Principles

- REST-first API design
- Stateless services
- Separation of concerns
- Avoid exposing entity models directly (use DTOs)
- Centralized exception handling (planned)

---

## 📦 Core Domain (Planned)

Entities:
- User
- Portfolio
- Stock
- Trade

Future Enhancements:
- Order history
- Watchlist
- Notifications

---

## 🔗 API Configuration

Base URL (local):
http://localhost:8080/api

Frontend URL:
https://paperstockindia.netlify.app

---

## ⚙️ Environment Configuration

### application.properties
server.port=8080
spring.data.mongodb.uri=mongodb://localhost:27017/paperstock

---

## 🌐 CORS Configuration (Required)

Frontend (Netlify) will call backend APIs.

Must allow:
- Origin: https://paperstockindia.netlify.app
- Methods: GET, POST, PUT, DELETE

---

## 🚀 Current Status

- ✅ Frontend deployed (Netlify)
- ✅ Backend repository initialized
- ⏳ Spring Boot project setup pending
- ⏳ MongoDB integration pending

---

## 🎯 Current Tasks

1. Initialize Spring Boot project
2. Verify application runs
3. Create test endpoint:
   GET /api/hello

Expected response:
"Backend is working!"

---

## 📌 Development Roadmap

### Phase 1 (Setup)
- Spring Boot initialization
- Basic controller
- MongoDB connection

### Phase 2 (Core Features)
- User APIs (register/login)
- Portfolio APIs
- Trade simulation

### Phase 3 (Enhancements)
- DTO layer
- Exception handling
- Validation

### Phase 4 (Performance)
- Redis caching
- Query optimization

### Phase 5 (Deployment)
- Deploy backend (Render / Railway)
- Connect frontend → backend

---

## 🛠 Common Commands

Run application:mvn spring-boot:run

Test API:http://localhost:8080/api/hello


---

## ⚠️ Known Constraints

- No authentication yet
- No caching implemented
- No security layer (Spring Security not added)

---

## 🧩 Engineering Notes

- Keep frontend and backend fully decoupled
- No Git submodules
- Maintain clean API contracts
- Use environment-specific configs (future: prod vs dev)

---

## 💡 Developer Guidance

- Build minimal working APIs first
- Avoid premature optimization
- Keep code readable and modular
- Add features incrementally
- Validate API using Postman before frontend integration

---

## 🔥 Next Immediate Action

- Generate Spring Boot project via https://start.spring.io
- Add dependencies
- Run backend locally
- Create `/api/hello` endpoint

---

## 🧠 Claude Usage Notes

- This file provides persistent project context
- Use it to maintain consistency across sessions
- Update this file as architecture evolves