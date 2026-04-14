# UCAN — Luxury Fictional E-commerce Platform

This repository contains a fullstack scaffold for **UCAN**, a premium fake e-commerce experience with realistic UX flows, email verification, fictional balances, checkout, reusable fake cards, and protected hidden admin operations.

## 1) Concise implementation plan

1. Create a monorepo with React frontend and Java Spring Boot backend.
2. Define Supabase/PostgreSQL schema and seed luxury categories/subcategories/products.
3. Implement auth (signup/login/logout/session), email verification and one-time bonus logic.
4. Implement advanced catalog search + combined filters + sorting.
5. Implement cart, checkout, address capture, fake card storage/reuse, and order creation.
6. Add user dashboard data aggregation and hidden admin dashboard protected in frontend/backend.
7. Apply premium responsive UI for desktop/tablet/mobile.

## 2) Folder structure

```text
.
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   ├── contexts/
│   │   ├── pages/
│   │   │   ├── admin/
│   │   │   ├── auth/
│   │   │   ├── catalog/
│   │   │   ├── checkout/
│   │   │   └── dashboard/
│   │   ├── services/
│   │   └── styles/
│   └── ...
├── backend/
│   ├── src/main/java/com/ucan/
│   │   ├── config/
│   │   ├── controller/
│   │   ├── security/
│   │   └── service/
│   └── src/main/resources/application.yml
└── db/migrations/
    ├── 001_schema.sql
    └── 002_seed_luxury_catalog.sql
```

## 3) Database schema (Supabase/PostgreSQL)

Implemented tables:

- profiles
- user_roles
- user_balances
- email_verification_tokens
- email_bonus_logs
- categories
- subcategories
- brands
- product_types
- colors
- products
- product_images
- product_videos
- carts
- cart_items
- fake_cards
- saved_addresses
- orders
- order_items
- order_addresses
- order_status_history
- favorites
- reviews
- banners
- featured_sections
- notifications
- admin_logs

## 4) Frontend scaffold

- React + Vite app with responsive premium styling.
- jQuery integrated for catalog grid transition behavior.
- Routes/pages for:
  - Home
  - Catalog with advanced filters
  - Login / Signup / Verify email
  - Cart / Checkout
  - User dashboard
  - Hidden admin route (`/ops-console-7f9x`)
- Route guard for auth and admin-only areas.

## 5) Backend scaffold

- Spring Boot Web + Security + JDBC + Validation + Mail.
- JWT stateless auth filter and role-based path protection.
- Controllers:
  - `/api/auth/*`
  - `/api/catalog/*`
  - `/api/checkout/*`
  - `/api/users/me/dashboard`
  - `/api/admin/metrics`
- Services for auth, catalog filtering, checkout/orders, user dashboard, and email sending.

## 6) Core business rules implemented

- Signup creates profile and grants **R$ 100.000,00** fictional starting balance.
- Signup sends verification email with token link.
- Email verification:
  - marks email verified
  - grants **R$ 100.000.000,00** once
  - logs grant in `email_bonus_logs`
  - prevents duplicate verification bonus
- Checkout:
  - creates orders + order items
  - stores shipping address
  - supports new fake card or saved card reuse
  - deducts fictional balance
  - sends purchase confirmation email

## 7) Luxury seed data

- Seed includes all required luxury categories.
- Includes representative subcategories and generated premium products.
- Each product includes:
  - name/category/subcategory/brand/type/color
  - fictional price
  - short + long descriptions
  - specifications JSON
  - photo + video placeholders
  - fictional stock
  - premium badge + featured flags
  - fictional delivery time

## 8) Advanced catalog filters/search

Implemented combined filters by:

- product name
- category
- subcategory
- brand
- type
- color

Sorting:

- featured
- price ascending
- price descending
- newest

## 9) Auth, verification, and bonus logic

- Real signup/login flow via backend auth endpoints.
- JWT-based session handling on frontend.
- Verification endpoint consumes email token and grants one-time bonus.
- Email flows are wired through SMTP configuration.

## 10) Checkout and fake card flow

- Functional cart context.
- Shipping capture during checkout.
- Fake card basic format validation in frontend.
- Fake cards persisted and reusable.
- Order persisted and confirmation email sent.

## 11) Hidden admin dashboard and guards

- Frontend hidden route path and admin-only guard.
- Backend enforces `/api/admin/**` with `ROLE_ADMIN`.
- Admin page intentionally excluded from public nav semantics (only low-visibility ops link shown for admin users).

## 12) Responsive behavior

- Breakpoints for desktop, tablet, and mobile.
- Grid/layout collapse for catalog, filters, metrics, nav and checkout blocks.

---

## Run locally

### Backend

```bash
cd backend
mvn spring-boot:run
```

Set env vars for Supabase DB and SMTP before running:

- `SUPABASE_DB_URL`
- `SUPABASE_DB_USER`
- `SUPABASE_DB_PASSWORD`
- `APP_JWT_SECRET`
- `FRONTEND_URL`
- `SMTP_HOST` `SMTP_PORT` `SMTP_USER` `SMTP_PASS`

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Optional:
- `VITE_API_URL=http://localhost:8080/api`

---

## Notes

- Monetary values and payments are fictional as requested.
- Emails are real SMTP sends if configured with a valid provider.
- For production Supabase usage, apply migrations under `db/migrations` to your project database.
