# ShopSphere — E-commerce Application

A full-stack e-commerce demo built with **React**, **Spring Boot**, and **PostgreSQL**. It includes a product catalogue, product administration, a shopping cart, and a demo order workflow.

> **Note:** Checkout is a demonstration flow. It does not process real payments.

## Features

- Browse and search the product catalogue
- Admin product create, read, update, and delete operations
- Add products to a shopping cart
- Create demo orders through checkout
- Validate product stock during order creation
- View created orders

## Technology Stack

| Layer | Technology |
|---|---|
| Frontend | React, Vite |
| Backend | Java 21, Spring Boot 3.4.5 |
| API | Spring Boot REST |
| Database | PostgreSQL |
| Containerization | Docker, Docker Compose |

## Architecture

```text
React + Vite
     |
     | HTTP / REST
     v
Spring Boot REST API
     |
     | JPA
     v
PostgreSQL
```

## Screenshots

### Storefront

![ShopSphere Storefront](screenshots/storefront.jpeg)

### Admin Panel

![ShopSphere Admin Panel](screenshots/admin.jpeg)

## Run Locally with Docker

### Prerequisites

- Docker Desktop
- Git

### Start the application

```bash
git clone https://github.com/codewithsayan7/shopsphere-springboot-react.git
cd shopsphere-springboot-react
docker compose up --build
```

### Access the application

| Service | URL |
|---|---|
| React frontend | http://localhost:5174 |
| Backend API | http://localhost:8081 |
| Products API | http://localhost:8081/api/products |

To stop the services:

```bash
docker compose down
```

This keeps the database volume. Use `docker compose down -v` only if you intentionally want to remove persisted database data.

## Project Structure

```text
shopsphere-springboot-react/
├── backend/
├── frontend/
├── screenshots/
├── docker-compose.yml
├── .gitignore
└── README.md
```

## What This Project Demonstrates

- Designing REST API-backed application workflows
- Implementing product management operations
- Building cart and order flows
- Validating inventory during checkout
- Connecting Spring Boot to PostgreSQL
- Developing a React frontend
- Containerizing services with Docker Compose

## Important Scope

This is a portfolio/demo application. The checkout flow creates demo orders and validates stock; it is not connected to a payment gateway.

## Future Improvements

- User authentication and role-based authorization
- Persistent user-specific carts
- Payment gateway integration
- Automated backend and frontend tests
- Production deployment and CI/CD

## Author

**Sayan Banerjee**

GitHub: [@codewithsayan7](https://github.com/codewithsayan7)
