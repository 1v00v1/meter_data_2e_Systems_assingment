# 🌐 Meter Data System

A **Spring Boot 3.5** application for loading, processing, and serving **meter (METAR) and weather data** with subscription support.  
The system provides REST APIs, scheduled tasks, caching, and persistence in a **MySQL database**.

---

## ✨ Features

- REST APIs for:
  - **METAR data ingestion**
  - **Weather retrieval by ICAO code**
  - **Subscription management**
- **Scheduler** to periodically fetch & persist METAR data.
- **Database initialization** with ICAO codes.
- **Caching & JSON filtering** for optimized responses.
- Configurable via `application.properties`.
- **Dockerfile** for containerized deployment.

---

## 🛠 Tech Stack

- **Java 21**
- **Spring Boot 3.5**
  - Spring Web
  - Spring Data JPA
  - Spring Cache
- **Jackson** for JSON serialization
- **Maven** for build management
- **MySQL** (primary database)
- **Docker** (optional)

---

## ⚙️ Prerequisites

- JDK 21+
- Maven 3.9+
- MySQL 8+
- (Optional) Docker

---

