# PROJECT SPECIFICATION

**Project Title**: Enterprise Pharmacy E-Commerce Backend (Store Side Only)  
**System Type**: Enterprise RESTful Web API Service Platform  
**Architecture Model**: Clean Architecture, Layered Architecture, Microservice-Ready Monolith  
**Version**: 1.0.0  

---

## 1. Executive Summary

The **Enterprise Pharmacy E-Commerce Backend** is a scalable, highly available, production-grade web service platform tailored for digital healthcare marketplaces and online pharmacy retail stores. Inspired by modern online healthcare platforms such as Arogga, the system provides customer-facing ordering workflows, digital prescription validation pipelines, real-time inventory batch tracking, prescription vs. over-the-counter (OTC) drug handling, cold-chain delivery management, localized payment processing, and comprehensive operational auditing.

The system is designed with Java 17 LTS, Spring Boot 3.3.5, PostgreSQL 16, Redis 7, MinIO S3 object storage, and Docker containerization. It delivers sub-100ms response times for catalog browsing, sub-50ms cache retrieval for trending healthcare searches, and zero-downtime database migrations via Flyway.

---

## 2. Business Goals

1. **Digital Healthcare Accessibility**: Streamline online ordering of authentic prescription medicines, OTC products, healthcare equipment, personal care items, and wellness supplements.
2. **Regulatory & Prescription Safety Compliance**: Enforce mandatory pharmacist verification workflows for prescription-required drugs prior to order dispatch.
3. **High Scalability & Reliability**: Support peak load throughput of 10,000+ concurrent customer shopping sessions with 99.99% service availability.
4. **Transparent Order & Logistics Tracking**: Provide real-time delivery status updates from warehouse dispatch to customer doorstep delivery.
5. **Data Security & Privacy Compliance**: Protect sensitive patient healthcare records, uploaded prescription documents, and financial transactions using AES-256 encryption at rest and TLS 1.3 in transit.

---

## 3. Project Scope

The scope of this project encompasses the **Store-Side (Customer & Store Operations) Backend System**:

- **Authentication & Security**: Multi-factor OTP/password authentication, JWT token issuance/refresh/revocation, Role-Based Access Control (`ROLE_CUSTOMER`, `ROLE_MODERATOR`, `ROLE_ADMIN`, `ROLE_PHARMACIST`).
- **User & Address Management**: Profile management, multi-address book with default shipping/billing designations, and emergency contact details.
- **Product Catalog Management**: Pharmaceutical catalog supporting generic names, dosage forms, strength variations, manufacturer data, prescription requirements, and media attachments.
- **Category & Brand Management**: Hierarchical category taxonomy (e.g., Prescription Medicines -> Chronic Care -> Diabetes Care) and brand directory.
- **Search & Filter Engine**: Multi-criteria dynamic search with generic name indexing, dosage filtering, price range, manufacturer filtering, and search autocomplete.
- **Inventory & Stock Management**: Real-time warehouse inventory tracking, stock batch expiration management, reserved stock locking, and low-stock alerts.
- **Cart & Wishlist**: Persistent shopping cart calculation, save-for-later wishlist management, and stock availability checks.
- **Promotions & Coupons**: Fixed/percentage discount validation, minimum spend thresholds, usage caps, and expiration enforcement.
- **Checkout & Order Management**: Prescription upload verification, multi-item order placement, order status transitions (`PENDING`, `PRESCRIPTION_VERIFIED`, `PROCESSING`, `SHIPPED`, `DELIVERED`, `CANCELLED`), order cancellation, and timeline logs.
- **Payments**: Cash on Delivery (COD), mobile financial service (MFS) integrations, payment gateway callbacks, transaction audit trails, and refund handling.
- **Delivery & Logistics**: Express delivery calculation, zone-based shipping rules, rider assignment, tracking numbers, and delivery status updates.
- **Reviews & Ratings**: Verified purchaser reviews, rating aggregation, and moderation flag controls.
- **Notifications**: Automated asynchronous email notifications (welcome, password reset, order confirmation, status updates).
- **Admin & Operational Dashboard**: Summary metrics, inventory movement analytics, purchase order fulfillment, and automated system schedulers.
- **Audit & System Health**: Comprehensive audit trail (`createdBy`, `updatedBy`, `deletedBy`), logging correlation (`requestId`, `traceId`), and Prometheus/Actuator monitoring endpoints.

---

## 4. Out of Scope

Unless explicitly requested in future phases, the following components are **Out of Scope**:

1. Enterprise Resource Planning (ERP) financial general ledgers.
2. Human Resource Management System (HRMS) & payroll.
3. Point-of-Sale (POS) physical hardware register drivers.
4. Vendor/Supplier Self-Service Portal frontend.
5. Native Mobile (iOS/Android) mobile app clients.
6. Telemedicine video consultation or real-time doctor appointment engines.

---

## 5. User Roles

| Role Name | Authority Identifier | Scope & Responsibilities |
| :--- | :--- | :--- |
| **Customer** | `ROLE_CUSTOMER` | Browse catalog, upload prescriptions, manage cart/wishlist, place orders, make payments, track shipments, submit reviews. |
| **Pharmacist** | `ROLE_PHARMACIST` | Review uploaded prescription images, verify prescription authenticity, approve or reject prescription-required order items. |
| **Rider / Delivery Agent** | `ROLE_RIDER` | Accept assigned delivery shipments, update delivery progress, confirm Cash on Delivery (COD) collection. |
| **Moderator** | `ROLE_MODERATOR` | Moderate customer reviews, manage product catalog listings, verify supplier profiles. |
| **Administrator** | `ROLE_ADMIN` | Full system access: user privilege management, system configuration, purchase orders, cache control, scheduler execution, audit logs. |

---

## 6. Functional Requirements

### 6.1 Authentication & User Management
- **FR-AUTH-01**: System shall allow user registration with valid email, phone number, and password.
- **FR-AUTH-02**: System shall issue short-lived JWT access tokens and long-lived refresh tokens upon successful login.
- **FR-AUTH-03**: System shall support token revocation/blacklisting upon user logout.
- **FR-USER-01**: Users shall manage multiple delivery addresses with default shipping and billing flags.

### 6.2 Pharmaceutical Product Catalog
- **FR-CAT-01**: Products shall support generic name, dosage form (tablet, capsule, syrup, injection), strength (e.g., 500mg, 10ml), and manufacturer metadata.
- **FR-CAT-02**: Products shall be flagged as `prescriptionRequired = true` for controlled medicines.
- **FR-CAT-03**: Products shall support soft deletion, preserving historical order references.

### 6.3 Search & Discovery
- **FR-SRCH-01**: System shall perform multi-criteria searches filtering by brand, category, generic name, price, dosage form, and stock status.
- **FR-SRCH-02**: System shall provide autocomplete suggestions powered by Redis cache regions.

### 6.4 Cart, Checkout & Prescription Workflow
- **FR-ORD-01**: Cart shall recalculate item totals, applied tax rates, delivery charges, and coupon discounts in real time.
- **FR-ORD-02**: Orders containing `prescriptionRequired = true` items must require uploading valid prescription images stored securely in MinIO S3 object storage.
- **FR-ORD-03**: Orders requiring prescriptions shall enter `PENDING_PRESCRIPTION_APPROVAL` status until verified by a licensed pharmacist.

### 6.5 Payments & Logistics
- **FR-PAY-01**: System shall process Cash on Delivery (COD), Mobile Financial Services (MFS), and Digital Credit Card payments with transaction audit records.
- **FR-DEL-01**: System shall assign riders, calculate delivery fees based on zone distance and delivery speed (Standard vs. Express), and generate tracking numbers (`TRK-...`).

---

## 7. Non-Functional Requirements

### 7.1 Performance & Scalability
- **NFR-PERF-01**: API response times for cached catalog GET requests shall remain under 50ms at P95 load.
- **NFR-PERF-02**: Database write transactions shall complete within 200ms at P99 load.
- **NFR-PERF-03**: System shall support Redis caching for product details, categories, brands, trending searches, and dashboard metrics.

### 7.2 Security & Compliance
- **NFR-SEC-01**: Passwords must be hashed using BCrypt with minimum cost factor of 10.
- **NFR-SEC-02**: All REST endpoints must be secured using Spring Security role-based access control (`@PreAuthorize`).
- **NFR-SEC-03**: Sensitive logs must be automatically masked to prevent logging credit cards, passwords, and access tokens.

### 7.3 High Availability & Maintainability
- **NFR-MAINT-01**: Every module must follow Clean Layered Architecture (`controller`, `service`, `repository`, `entity`, `dto`, `mapper`, `specification`, `validator`).
- **NFR-MAINT-02**: Database schema changes must be versioned via Flyway migrations (`V1__...`).

---

## 8. Assumptions

1. Modern web browsers and API client applications support TLS 1.3 and standard JSON request payloads.
2. MinIO S3 object storage service is accessible over local container or cloud network bridges.
3. Licensed pharmacists are available to verify digital prescriptions prior to fulfillment dispatch.

---

## 9. Constraints

1. **Technology Mandate**: Implementation must exclusively use Java 17 LTS, Spring Boot 3.3.5, PostgreSQL 16, Redis 7, and Docker.
2. **Zero Entity Leakage**: JPA `@Entity` classes must never be returned directly across REST controller boundaries.
3. **No Field Injection**: All Spring component dependencies must use constructor injection (`@RequiredArgsConstructor`).

---

## 10. Success Criteria

1. **Clean Compilation**: Zero compiler warnings or build failures across all project modules.
2. **100% Automated Test Suite Passing**: 100% pass rate on unit, repository, controller, security, and integration test suites.
3. **Zero Security Vulnerabilities**: Clean security authorization rules preventing unauthorized escalation of privilege across all endpoints.
4. **Complete OpenAPI Specification**: Interactive Swagger UI available at `/swagger-ui.html` documenting all REST operations.

---

## 11. Risks & Mitigation

| Identified Risk | Impact | Mitigation Strategy |
| :--- | :--- | :--- |
| **Prescription Verification Bottleneck** | High | Implement asynchronous email/SMS notifications alerting pharmacists of pending approval queues. |
| **N+1 Database Query Overhead** | High | Enforce JPA Join Fetch / Entity Graphs and `@BatchSize` collection configurations. |
| **Over-caching Stale Stock Data** | Medium | Implement programmatic `@CacheEvict` triggers on stock update events. |

---

## 12. Future Scope

1. Integration with Elasticsearch / OpenSearch for AI-assisted symptom-to-medicine search.
2. Integration with automated SMS gateways for delivery rider notification broadcasts.
3. Machine learning models for refill reminders based on customer prescription dosage schedules.

---

## 13. Acceptance Criteria

- [x] Executive Summary, Scope, Roles, Functional and Non-Functional requirements fully specified.
- [x] Prescription validation workflow defined with pharmacist verification flags.
- [x] Zero placeholders or incomplete sections in documentation.
- [x] System design complies with Clean Architecture, Layered Architecture, and SOLID principles.
