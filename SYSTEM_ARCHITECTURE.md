# SYSTEM ARCHITECTURE

**Project Title**: Enterprise Pharmacy E-Commerce Backend  
**Document**: Architecture Blueprint & System Design Specification  
**Architecture Model**: Clean Architecture, Layered Architecture, Microservice-Ready Monolith  
**Version**: 1.0.0  

---

## 1. Executive Summary

The **Enterprise Pharmacy E-Commerce Backend** is built on Clean Architecture and Layered Architecture principles. Designed as a microservice-ready modular monolith, it enforces strict separation of concerns between HTTP presentation controllers, core domain business logic, Data Transfer Objects (DTOs), persistence entity models, database access repositories, and external infrastructure integrations.

The architecture emphasizes high availability, zero N+1 database query traps, sub-50ms catalog cache lookups via Redis 7, digital prescription document management via MinIO S3, resilient background job processing via Spring Scheduler, and seamless containerization using Docker Compose.

---

## 2. Architecture Overview

The system architecture partitions concerns into five distinct structural layers:

1. **Client / Web API Layer**: Exposes RESTful JSON endpoints under `/api/v1/...` protected by Spring Security filters and documented via OpenAPI v3 / Swagger UI.
2. **Controller Layer**: Handles incoming HTTP requests, enforces Jakarta Bean Validation, validates authorization roles (`ROLE_CUSTOMER`, `ROLE_PHARMACIST`, `ROLE_ADMIN`), and delegates execution to service interfaces.
3. **Service Layer (`ServiceImpl`)**: Houses 100% of domain business logic, transactional boundaries (`@Transactional`), cache eviction rules, and domain event publishing.
4. **Data Access Layer (`Repository` & `Specification`)**: Executes type-safe queries using Spring Data JPA, Hibernate, B-Tree indexed PostgreSQL tables, and dynamic multi-criteria Specifications.
5. **Infrastructure Layer**: Connects to Redis 7 (caching), MinIO S3 (prescription object storage), SMTP (asynchronous HTML email delivery), and Spring Boot Actuator/Prometheus (observability).

---

## 3. Clean Architecture

Clean Architecture principles isolate core business logic from external frameworks, database ORMs, and web drivers.

```mermaid
graph TD
    subgraph "External Infrastructure Layer"
        UI["REST Controllers / Swagger UI"]
        DB["PostgreSQL Database"]
        Cache["Redis Cache"]
        Storage["MinIO S3 Storage"]
    end

    subgraph "Application Service Layer"
        UseCases["Business Use Cases (ServiceImpl)"]
        EventBus["Domain Event Publisher"]
    end

    subgraph "Domain Core Layer"
        Entities["Domain Entities & Rules"]
        ValueObjects["Value Objects & Enums"]
        Interfaces["Repository Interfaces"]
    end

    UI --> UseCases
    UseCases --> Entities
    UseCases --> Interfaces
    DB -. implements .-> Interfaces
    Cache -. integrates .-> UseCases
    Storage -. integrates .-> UseCases
```

- **Dependency Inversion Principle**: Higher-level business use cases depend on domain repository interfaces, not concrete PostgreSQL data access classes.
- **Framework Independence**: Business validation and state transitions run independently of web framework details.

---

## 4. Layered Architecture

The physical package structure reflects a strict top-down execution flow:

```
com.example.ecommerce.module/
├── controller/        --> Request Validation, Security Authorization, ApiResponse Envelopes
├── service/           --> Service Interfaces
│   └── impl/          --> Business Logic, Transaction Boundaries, Cache Eviction
├── repository/        --> JPA Repositories extending BaseRepository
├── entity/            --> JPA Entities extending BaseEntity
├── dto/               --> Request & Response DTOs
├── mapper/            --> MapStruct Mappers
├── specification/     --> Dynamic Search Specifications
└── validator/         --> Business Rule & State Transition Validation
```

```mermaid
sequenceDiagram
    autonumber
    actor Client
    participant Controller as Controller Layer
    participant Validator as Validator Layer
    participant Service as Service Layer (ServiceImpl)
    participant Mapper as MapStruct Mapper
    participant Repo as Repository Layer
    participant DB as PostgreSQL DB

    Client->>Controller: HTTP Request (JSON + JWT)
    Controller->>Validator: Validate Request DTO
    Validator-->>Controller: Input Validated
    Controller->>Service: Execute Use Case Method
    Service->>Repo: Query Database with Specification
    Repo->>DB: SQL Query Execution
    DB-->>Repo: ResultSet / Entity
    Repo-->>Service: Domain Entity
    Service->>Mapper: Map Entity to Response DTO
    Mapper-->>Service: Response DTO
    Service-->>Controller: Execution Result
    Controller-->>Client: ApiResponse<T> Envelope (200 OK)
```

---

## 5. Component Diagram

The component topology highlights module boundaries and external system drivers.

```mermaid
graph TB
    subgraph "Spring Boot Application Container"
        Security["Spring Security & JWT Filter"]
        
        subgraph "Domain Modules"
            AuthMod["Auth & Security"]
            ProductMod["Product Catalog & Search"]
            CartMod["Cart & Wishlist"]
            OrderMod["Checkout & Order Engine"]
            DeliveryMod["Logistics & Delivery"]
            SupplierMod["Supplier & Purchase"]
            AuditMod["Audit & Logging"]
        end
        
        Scheduler["Spring Scheduler Cron Jobs"]
        EmailEngine["Async Thymeleaf Mail Engine"]
    end

    subgraph "Data & Persistence Tier"
        PG[(PostgreSQL 16 DB)]
        Redis[(Redis 7 Cache)]
        MinIO[(MinIO S3 Object Storage)]
    end

    Client[Web Browser / Mobile App] --> Security
    Security --> AuthMod
    Security --> ProductMod
    Security --> CartMod
    Security --> OrderMod
    Security --> DeliveryMod
    Security --> SupplierMod
    Security --> AuditMod

    ProductMod --> Redis
    CartMod --> Redis
    OrderMod --> PG
    OrderMod --> MinIO
    OrderMod --> EmailEngine
    SupplierMod --> PG
    AuditMod --> PG
    Scheduler --> ProductMod
```

---

## 6. Module Interaction Diagram

The diagram below illustrates cross-module dependency paths during shopping and order placement.

```mermaid
graph LR
    Auth["Auth Module"] --> User["User Module"]
    Product["Product Module"] --> Category["Category Module"]
    Product --> Brand["Brand Module"]
    Cart["Cart Module"] --> Product["Product Module"]
    Cart --> Coupon["Coupon Module"]
    Order["Order Module"] --> Cart["Cart Module"]
    Order --> User["User Module"]
    Order --> Address["Address Module"]
    Order --> Delivery["Delivery Module"]
    Payment["Payment Module"] --> Order["Order Module"]
    Delivery --> Order["Order Module"]
    Notification["Notification Module"] --> Email["Email Module"]
    Order --> Notification["Notification Module"]
```

---

## 7. Authentication Flow

Authentication uses JWT Access Tokens (short-lived) and Refresh Tokens (long-lived) with revocation tracking.

```mermaid
sequenceDiagram
    autonumber
    actor User
    participant Gateway as Security Filter Chain
    participant AuthCtrl as AuthController
    participant AuthService as AuthServiceImpl
    participant JwtService as JwtService
    participant Redis as Redis Revocation Store

    User->>AuthCtrl: POST /api/v1/auth/login (email, password)
    AuthCtrl->>AuthService: authenticate(LoginRequest)
    AuthService->>AuthService: Validate Credentials (BCrypt)
    AuthService->>JwtService: generateAccessToken(userDetails)
    AuthService->>JwtService: generateRefreshToken(userDetails)
    AuthService-->>AuthCtrl: AuthResponse (accessToken, refreshToken)
    AuthCtrl-->>User: ApiResponse<AuthResponse> (200 OK)

    Note over User, Gateway: Subsequent Protected Request
    User->>Gateway: GET /api/v1/orders (Header: Authorization: Bearer <token>)
    Gateway->>Redis: Check if Access Token Blacklisted
    Redis-->>Gateway: Token Active
    Gateway->>JwtService: Validate Signature & Claims
    JwtService-->>Gateway: Token Valid (User: john.doe, Roles: ROLE_CUSTOMER)
    Gateway-->>Gateway: Seed SecurityContextHolder
```

---

## 8. Checkout & Prescription Flow

Orders containing prescription-required medicines enter a dedicated validation workflow before fulfillment.

```mermaid
sequenceDiagram
    autonumber
    actor Customer
    participant OrderCtrl as OrderController
    participant OrderService as OrderServiceImpl
    participant Storage as StorageService (MinIO)
    participant Pharmacist as Pharmacist (ROLE_PHARMACIST)
    participant DeliveryService as DeliveryServiceImpl

    Customer->>OrderCtrl: POST /api/v1/orders (CartID, AddressID, PrescriptionImage)
    OrderCtrl->>Storage: uploadPrescription(file)
    Storage-->>OrderCtrl: File URL (s3://prescriptions/rx-12345.jpg)
    OrderCtrl->>OrderService: createOrder(CreateOrderRequest)
    
    alt Contains Prescription Medicines
        OrderService->>OrderService: Set Status: PENDING_PRESCRIPTION_APPROVAL
        OrderService-->>Customer: ApiResponse (201 Created - Pending Verification)
        
        Pharmacist->>OrderService: GET /api/v1/orders/pending-prescription
        Pharmacist->>OrderService: POST /api/v1/orders/{id}/verify-prescription (APPROVED)
        OrderService->>OrderService: Update Status: PRESCRIPTION_APPROVED
    else OTC Medicines Only
        OrderService->>OrderService: Set Status: CONFIRMED
    end

    OrderService->>DeliveryService: createShipment(OrderID)
    DeliveryService-->>OrderService: Tracking Number (TRK-98765)
```

---

## 9. Order & Payment Flow

```mermaid
stateDiagram-v2
    [*] --> PENDING_PAYMENT : Order Created
    PENDING_PAYMENT --> PAYMENT_COMPLETED : Payment Success (COD / Card / MFS)
    PENDING_PAYMENT --> CANCELLED : Payment Failed / Timed Out
    PAYMENT_COMPLETED --> PROCESSING : Warehouse Packing Started
    PROCESSING --> SHIPPED : Rider Assigned & Dispatched
    SHIPPED --> DELIVERED : Doorstep Delivery Confirmed
    DELIVERED --> [*]
    CANCELLED --> [*]
```

---

## 10. Notification Flow

Asynchronous email delivery prevents HTTP worker thread blocking.

```mermaid
sequenceDiagram
    autonumber
    participant Service as OrderServiceImpl / AuthServiceImpl
    participant AsyncEngine as @Async Spring Task Executor
    participant TemplateEngine as Thymeleaf SpringTemplateEngine
    participant MailSender as JavaMailSender (SMTP)

    Service->>AsyncEngine: sendOrderConfirmationEmailAsync(orderDTO)
    Note over Service: HTTP Thread Returns Response Immediately
    AsyncEngine->>TemplateEngine: process("order-confirmation.html", context)
    TemplateEngine-->>AsyncEngine: Compiled HTML Body
    AsyncEngine->>MailSender: send(MimeMessage)
    MailSender-->>AsyncEngine: Email Dispatched
```

---

## 11. Deployment Architecture

The application deploys as a multi-container Docker composition.

```mermaid
graph TB
    subgraph "Internet / Public Network"
        Clients["Clients (Browser / Mobile)"]
    end

    subgraph "Docker Bridge Network (ecommerce-network)"
        AppContainer["ecommerce-app (Spring Boot 3.3.5 / Java 17 JRE Alpine)"]
        PostgresContainer["ecommerce-postgres (PostgreSQL 16 Alpine)"]
        RedisContainer["ecommerce-redis (Redis 7 Alpine)"]
        MinioContainer["ecommerce-minio (MinIO S3 Storage)"]
    end

    subgraph "Persistent Volume Storage"
        PGData[("pgdata Volume")]
        RedisData[("redisdata Volume")]
        MinIOData[("miniodata Volume")]
    end

    Clients -- "Port 8080 (HTTP / REST)" --> AppContainer
    AppContainer -- "Port 5432 (JDBC)" --> PostgresContainer
    AppContainer -- "Port 6379 (RESP)" --> RedisContainer
    AppContainer -- "Port 9000 (S3 API)" --> MinioContainer
    PostgresContainer --> PGData
    RedisContainer --> RedisData
    MinioContainer --> MinIOData
```

---

## 12. Technology Stack Overview

| Tier / Category | Technology Selected | Version | Purpose & Rationale |
| :--- | :--- | :--- | :--- |
| **Language** | Java LTS | `17` | Long-term support, records, pattern matching, sealed types, improved G1GC performance. |
| **Framework** | Spring Boot | `3.3.5` | Modern web framework, Spring Security 6, Jakarta EE 10 compatibility. |
| **Relational DB** | PostgreSQL | `16` | ACID compliance, JSONB support, B-Tree indexes, Flyway migration safety. |
| **Cache & Store** | Redis | `7.0` | High-speed cache for catalog items, search autocomplete, and token blacklists. |
| **Object Storage** | MinIO S3 | `RELEASE...` | S3-compatible file storage for digital prescriptions and product media. |
| **Documentation** | Springdoc OpenAPI | `2.6.0` | Interactive OpenAPI 3 / Swagger documentation and testing. |
| **Build & Packaging** | Maven | `3.9` | Dependency management, multi-stage Docker builds, JaCoCo coverage reports. |

---

## 13. Scalability, High Availability & Performance Strategy

### 13.1 Scalability
- **Stateless App Tier**: JWT session management allows scaling Spring Boot application containers horizontally behind a load balancer without sticky sessions.
- **Read/Write Splitting**: Database read replicas can be added with zero changes to service code using Spring Data `@Transactional(readOnly = true)`.

### 13.2 High Availability & Resiliency
- **Redis AOF Persistence**: Redis is configured with Append-Only File (AOF) logging to prevent cache data loss on container restarts.
- **Graceful Shutdown**: Spring Boot configures `server.shutdown=graceful` with a 30-second shutdown phase to complete active transactions.

### 13.3 Performance Strategy
- **Catalog Caching**: Products (`products`), categories (`categories`), and brands (`brands`) are cached with dedicated TTLs (1h, 24h, 24h).
- **Index Optimization**: B-Tree indexes on `sku`, `slug`, `category_id`, `brand_id`, `status`, and `deleted` eliminate full table scans.
- **N+1 Prevention**: JPA Join Fetch and `@BatchSize(size = 25)` configurations enforce bulk fetching.

---

## 14. Security Strategy

1. **Role-Based Access Control (RBAC)**: Endpoint method security (`@PreAuthorize("hasRole('ADMIN')")`) protects administrative APIs.
2. **Input Validation**: All incoming request DTOs are validated using Jakarta Bean Validation constraints (`@Valid`).
3. **Data Protection**: Sensitive parameters (passwords, JWT tokens, credit card details) are masked in SLF4J logs by `LoggingMaskUtils`.
4. **Non-Root Execution**: Containerized Spring Boot runner stages execute under non-root system account `ecommerce` (UID 10001).
