# 📌 AI Resume Analyzer (ATS Checker)

An AI-powered Resume Analyzer built using **Java, Spring Boot, Spring Security, Spring AI, REST APIs, and MySQL**.  
The system evaluates resumes using ATS-based scoring logic and AI-driven content analysis.

---

## 🚀 Features

- 🔍 Resume parsing using Apache Tika  
- 🤖 AI-based resume evaluation via OpenAI (Spring AI)  
- 📊 Custom ATS scoring logic (keyword matching & formatting checks)  
- 🔐 Secure authentication using Spring Security (BCrypt password hashing)  
- 📧 Email-based registration and login  
- 🗄 Persistent storage using MySQL & JPA (Hibernate ORM)  
- 🧱 Clean layered architecture (Controller–Service–Repository)

---

## 🏗 Architecture

The project follows a layered architecture:

Controller → Service → Repository → Database

- **Controller** → Handles REST API requests  
- **Service** → Business logic & ATS scoring  
- **Repository** → Data access using JPA  
- **Security Layer** → Authentication & password encryption  

---

## 🔐 Authentication Flow

1. User registers with username, email, and password  
2. Password is stored securely using BCrypt hashing  
3. Login verifies credentials using Spring Security  
4. Stateless authentication structure (JWT-ready)

---

## 🛠 Tech Stack

- Java 21+
- Spring Boot 3
- Spring Security
- Spring Data JPA
- Spring AI (OpenAI integration)
- Apache Tika
- MySQL
- REST APIs

---

## 📡 API Endpoints

### 🔑 Authentication

### Register
POST /auth/register

Request:
```json
{
  "username": "string",
  "email": "string",
  "password": "string"
}
```

Response:
- 200 OK → User registered successfully  
- 400 Bad Request → Email already exists  

---

### Login
POST /auth/login

Request:
```json
{
  "email": "string",
  "password": "string"
}
```

Response:
- 200 OK → Login successful  
- 401 Unauthorized → Invalid email or password  

---

## 📂 Project Structure

```
src/main/java/com/example/RapidResume
│
├── Controller
├── Service
├── Repository
├── Entity
├── Configuration
```

---

## ⚙️ Setup & Run

### 1️⃣ Clone Repository

```bash
git clone <your-repo-link>
cd RapidResume
```

### 2️⃣ Configure MySQL

Create database:

```sql
CREATE DATABASE rapidresume;
```

Update `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/rapidresume
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### 3️⃣ Run Application

```bash
mvn spring-boot:run
```

Server runs at:

```
http://localhost:8080
```

---

## 🎯 Future Improvements

- JWT-based authentication  
- Resume history tracking  
- Role-based access control  
- Cloud deployment (Docker / AWS)

---

## 👨‍💻 Author

Abhishek  
BCA Final Year | Java Full Stack Developer
