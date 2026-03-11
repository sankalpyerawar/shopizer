# Shopizer Repository Summary

## Overview
**Shopizer** is an open-source Java-based headless e-commerce platform providing REST APIs for building modern commerce applications.

- **Version**: 3.2.7 (codebase shows 3.2.5)
- **Java Version**: Java 11, 17+ supported
- **Framework**: Spring Boot 2.5.12
- **License**: Apache License 2.0
- **Official Site**: http://www.shopizer.com

## Architecture

### Multi-Module Maven Project
The repository follows a modular architecture with 5 main modules:

```
shopizer/
├── sm-core-model/       # Core domain models and entities
├── sm-core-modules/     # Core business modules
├── sm-core/            # Core business logic and services
├── sm-shop-model/      # Shop-specific models and DTOs
└── sm-shop/            # Main Spring Boot application (REST API)
```

### Technology Stack
- **Backend**: Spring Boot, Hibernate/JPA
- **Database**: H2 (default), MySQL (configurable)
- **Search**: Elasticsearch 7.5.2
- **Build Tool**: Maven (with wrapper)
- **Containerization**: Docker support
- **API Documentation**: Swagger/OpenAPI

## Core Features

### E-commerce Capabilities
- **Catalog Management**: Products, categories, manufacturers
- **Shopping Cart**: Cart operations and management
- **Checkout**: Order processing and payment
- **Customer Management**: Customer accounts and profiles
- **Order Management**: Order tracking and fulfillment
- **Merchant/Store**: Multi-store support
- **User Management**: Admin and customer users
- **Inventory**: Stock management
- **Tax**: Tax calculation and management

### Headless Commerce
- RESTful API-first architecture
- Swagger UI for API exploration at `http://localhost:8080/swagger-ui.html`
- Separate frontend applications (React shop, Admin panel)

## Project Structure

### sm-shop (Main Application)
```
sm-shop/src/main/java/com/salesmanager/shop/
├── application/        # Spring Boot application entry point
├── store/             # Store-related controllers and facades
│   ├── api/          # REST API endpoints
│   ├── controller/   # Store controllers
│   ├── facade/       # Business facades
│   └── security/     # Security configurations
├── admin/            # Admin-specific functionality
├── controller/       # General controllers (Files, Images)
├── mapper/           # DTO to Entity mappers
├── populator/        # Entity to DTO populators
├── utils/            # Utility classes
├── filter/           # Request filters (CORS, XSS)
└── constants/        # Application constants
```

### sm-core (Business Logic)
```
sm-core/src/main/java/com/salesmanager/core/business/
├── services/         # Business services
├── repositories/     # Data access layer
├── modules/          # Integration modules
├── configuration/    # Business configurations
└── utils/           # Business utilities
```

### sm-core-model (Domain Models)
Core domain entities and JPA models for the e-commerce platform.

### sm-shop-model (API Models)
DTOs and API models for REST endpoints, including versioned models (v0, v1).

### sm-core-modules (Integration Modules)
Reusable business modules and integrations.

## Running the Application

### Docker (Quickest)
```bash
# Backend API
docker run -p 8080:8080 shopizerecomm/shopizer:latest

# Admin Panel
docker run -e "APP_BASE_URL=http://localhost:8080/api" -p 82:80 shopizerecomm/shopizer-admin

# React Shop
docker run -e "APP_MERCHANT=DEFAULT" -e "APP_BASE_URL=http://localhost:8080" -p 80:80 shopizerecomm/shopizer-shop-reactjs
```

### From Source
```bash
# Clone repository
git clone git://github.com/shopizer-ecommerce/shopizer.git

# Build entire project
cd shopizer
./mvnw clean install

# Run application
cd sm-shop
./mvnw spring-boot:run
```

### Access Points
- **API Documentation**: http://localhost:8080/swagger-ui.html
- **Default Port**: 8080
- **Database**: H2 in-memory (default), files in `sm-shop/`

## Configuration

### Application Properties
- Located at: `sm-shop/src/main/resources/application.properties`
- Default database schema: `SALESMANAGER`
- Max file upload: 4MB
- Max request size: 10MB
- Actuator endpoints enabled

### Database
- **Default**: H2 (in-memory)
- **Production**: MySQL/PostgreSQL (configurable)
- Database files: `SALESMANAGER.h2.db`, `SALESMANAGER.trace.db`

### Security
- Spring Security configured
- XSS filtering enabled
- CORS support
- JWT-based authentication (for API)

## Key Components

### REST API Structure
- Versioned APIs (v0, v1)
- Order management endpoints
- Product catalog endpoints
- Customer management endpoints
- Shopping cart endpoints
- Payment and transaction handling

### Data Persistence
- JPA/Hibernate for ORM
- Repository pattern for data access
- Support for multiple database vendors

### File Management
- Local file storage support
- Cloud storage integration
- Image handling for products

## Development

### Build System
- Maven multi-module project
- Maven wrapper included (`mvnw`, `mvnw.cmd`)
- Parent POM manages dependencies

### Testing
- Test coverage configured
- Unit tests in each module
- Integration tests available

### CI/CD
- CircleCI integration (`.circleci/config.yml`)
- Docker build support
- Automated builds configured

## Documentation & Support

- **Documentation**: https://shopizer-ecommerce.github.io/documentation/
- **Slack**: https://shopizer.slack.com
- **Community**: https://communityinviter.com/apps/shopizer/shopizer
- **Stack Overflow**: Tag `shopizer`
- **Contact**: http://www.shopizer.com/contact.html

## Contributing

1. Fork the repository
2. Clone your fork
3. Create a feature branch
4. Make changes and test
5. Push to your fork
6. Open a Pull Request

## Docker Hub
- **Backend**: `shopizerecomm/shopizer`
- **Admin**: `shopizerecomm/shopizer-admin`
- **Shop**: `shopizerecomm/shopizer-shop-reactjs`

## Additional Notes

- Headless architecture allows frontend flexibility
- Multi-tenant/multi-store capable
- Extensible through modules
- Production-ready with proper configuration
- Active community support
- Regular updates and maintenance

## File Storage
Default file storage location: `sm-shop/files/`
- `files/repos/` - Repository files
- `files/store/` - Store-specific files
