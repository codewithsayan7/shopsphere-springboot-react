# ShopSphere

ShopSphere is a small e-commerce demo I put together to practise a typical full-stack workflow: displaying products, managing a catalogue, building a cart, and placing an order through a REST API.

It is deliberately a demo rather than a real online shop. The checkout creates an order and updates stock, but it does **not** collect payment or handle customer accounts.

## What works in the demo

- Browse the product catalogue and search by product name or category.
- Add available products to a cart and review the total.
- Submit a demo order with a customer name and email address.
- Open the admin view to add, edit, and remove products.
- View orders created through the demo checkout.
- Check stock availability when an order is submitted.

## Technology

| Part | Technology |
|---|---|
| User interface | React and Vite |
| API | Java 21, Spring Boot 3.4.5 |
| Database access | Spring Data JPA |
| Database | PostgreSQL |
| Local development | Docker Compose |

## Request flow

The React frontend calls the Spring Boot API using JSON over HTTP. The backend handles product and order operations and persists the data in PostgreSQL.

```text
React + Vite
     |
     | HTTP / JSON
     v
Spring Boot REST API
     |
     | Spring Data JPA
     v
PostgreSQL
```

## Run locally

Install Git and Docker Desktop, then run:

```bash
git clone https://github.com/codewithsayan7/shopsphere-springboot-react.git
cd shopsphere-springboot-react
docker compose up --build
```

Once the containers are ready, use these addresses:

| Service | Address |
|---|---|
| Storefront | http://localhost:5174 |
| Backend API | http://localhost:8081 |
| Product catalogue | http://localhost:8081/api/products |
| Orders | http://localhost:8081/api/orders |

Stop the services with `docker compose down`. Your local database volume is kept. Running `docker compose down -v` removes that volume and its data, so use it only when you want a clean database.

## Screenshots

### Storefront

![ShopSphere storefront](screenshots/storefront.jpeg)

### Admin view

![ShopSphere admin](screenshots/admin.jpeg)

## Project structure

```text
backend/       Spring Boot API, persistence, and tests
frontend/      React storefront and admin interface
screenshots/   Screenshots of the application
docker-compose.yml
```

## What this project does not include

A few boundaries are worth being clear about:

- Checkout is a demonstration flow; there is no payment gateway.
- There is no customer login or account management.
- The admin view is a demo interface, not protected by production authentication.
- The project is intended for local portfolio demonstration, not handling real customer or payment data.

Those are possible next steps, but they are not implemented features today.

## What I wanted to practise

I used this project to work through how a frontend cart becomes an API request, how the backend checks product stock, and how product and order data are stored in a relational database. It also gave me a practical place to learn Docker Compose and connect a React app to a Spring Boot service.

## Author

**Sayan Banerjee** · [GitHub](https://github.com/codewithsayan7)
