# Shopizer E-Commerce Platform - Project Summary

## Overview

This workspace contains a complete headless e-commerce platform built with modern web technologies. The system follows a microservices architecture with three main components working together to provide a full-featured online shopping solution.

## Architecture

The platform uses a **headless commerce** approach, separating the backend business logic from the frontend presentation layers. This allows for flexible deployment and independent scaling of components.

## Components

### 1. Shopizer Backend (Java/Spring Boot)
**Location:** `/shopizer`  
**Technology:** Java 17+, Spring Boot 2.5.12  
**Version:** 3.2.7

The core backend API that powers the entire e-commerce platform. It provides RESTful APIs for all business operations.

**Key Features:**
- Product catalog management
- Shopping cart functionality
- Checkout and payment processing
- Merchant management
- Order processing
- Customer management
- User authentication and authorization

**Access:**
- API Documentation: http://localhost:8080/swagger-ui.html
- Runs on port 8080
- Docker image: `shopizerecomm/shopizer:latest`

### 2. Shopizer Shop (React.js)
**Location:** `/shopizer-shop-reactjs`  
**Technology:** React.js (Create React App)  
**Node Version:** v16.13.0

The customer-facing storefront where shoppers browse products, add items to cart, and complete purchases.

**Key Features:**
- Product browsing and search
- Shopping cart
- Checkout flow
- Customer account management
- Customizable theme colors
- Multi-merchant support

**Configuration:**
- Backend API configured in `public/env-config.js`
- Runs on port 3000 (development) or port 80 (Docker)
- Docker image: `shopizerecomm/shopizer-shop-reactjs`

### 3. Shopizer Admin (Angular)
**Location:** `/shopizer-admin`  
**Technology:** Angular 13.3.x  
**Node Version:** v12.22.7

The administrative dashboard for managing the e-commerce platform. Store owners and administrators use this to manage products, orders, customers, and store settings.

**Key Features:**
- Product and catalog management
- Order management
- Customer management
- Store configuration
- Merchant administration

**Access:**
- Runs on port 4200 (development) or port 80 (Docker)
- Default credentials:
  - Username: `admin@shopizer.com`
  - Password: `password`
- Docker image: `shopizerecomm/shopizer-admin`

## How It Works Together

1. **Backend API** (Shopizer) runs as the central service, handling all business logic, data persistence, and API endpoints
2. **Customer Storefront** (React) connects to the backend API to display products and process customer orders
3. **Admin Dashboard** (Angular) connects to the same backend API to manage store operations, products, and orders

All three components can be deployed independently using Docker containers, making the system highly scalable and maintainable.

## Deployment

Each component can be run locally for development or deployed via Docker:

```bash
# Backend
docker run -p 8080:8080 shopizerecomm/shopizer:latest

# Storefront
docker run -e "APP_BASE_URL=http://localhost:8080" -p 80:80 shopizerecomm/shopizer-shop-reactjs

# Admin
docker run -e "APP_BASE_URL=http://localhost:8080/api" -p 4200:80 shopizerecomm/shopizer-admin
```

## License

Apache License 2.0
