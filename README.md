# ShopSphere

ShopSphere is a small e-commerce demo built with React and Spring Boot. I made it to practise product management, shopping cart logic, and placing orders through a REST API.

## Features

- View and search products
- Add products to a shopping cart
- Place a demo order
- Check and lock product stock while an order is placed
- Add, edit and remove products from the admin view
- View orders placed through the demo

## Tech used

- Java 21
- Spring Boot
- React and Vite
- Spring Data JPA
- Spring Security (HTTP Basic admin access)
- PostgreSQL
- Docker Compose

## Run the project

You need Git and Docker installed. The local demo admin credentials are `admin` / `change-this-demo-password`. Change them before sharing or deploying the project. You can override them with `ADMIN_USERNAME` and `ADMIN_PASSWORD` environment variables.

```bash
git clone https://github.com/codewithsayan7/shopsphere-springboot-react.git
cd shopsphere-springboot-react
docker compose up --build
```

Then open:

- Storefront: http://localhost:5174
- Backend: http://localhost:8081
- Products API: http://localhost:8081/api/products
- Orders API: http://localhost:8081/api/orders

To stop the containers:

```bash
docker compose down
```

## Admin access

Product browsing and demo checkout are public. Product creation, editing, deletion and order viewing require the admin username and password configured through environment variables. The demo credentials are only for local practice; use a strong secret and HTTPS for any hosted environment.

Order totals use `BigDecimal`, and checkout locks product rows while checking and reducing stock to prevent two orders from overselling the same available quantity.

## Project folders

- `backend/` – Spring Boot API and tests
- `frontend/` – React storefront and admin page
- `screenshots/` – screenshots

## Screenshots

### Storefront

![ShopSphere storefront](screenshots/storefront.jpeg)

### Admin page

![ShopSphere admin](screenshots/admin.jpeg)

This is a demo project. Checkout does not collect payments, and the admin page does not have production login protection.

## Author

Sayan Banerjee – https://github.com/codewithsayan7
