## 2024-08-07 - [CRITICAL] Predictable Identifier Generation
**Vulnerability:** Found `java.util.Random` being used to generate Order Numbers, Invoice Numbers, and Transaction IDs in `OrderServiceImpl` and `PaymentServiceImpl`.
**Learning:** `java.util.Random` is a predictably seeded pseudo-random number generator. In contexts where identifiers must be unguessable (like transaction IDs or order IDs that might be exposed via URLs), using a predictable PRNG can lead to Insecure Direct Object References (IDOR) and information disclosure, allowing attackers to iterate through or predict identifiers.
**Prevention:** Always use a cryptographically strong pseudo-random number generator (CSPRNG) like `java.security.SecureRandom` when generating sensitive, unique, or unpredictable identifiers and tokens.
*   If running tests causes unrelated errors around `CacheManager`, it may be that `spring.cache.type=redis` is conditionally enabled but misconfigured in the application profile for tests, resulting in missing cache beans.
# Security Sentinel Logs

## Date: 2026-10-25
**Vulnerability Type:** Hardcoded Secrets (CWE-798)
**Component:** JWT Authentication Configuration
**Severity:** Critical
**Description:** A hardcoded, easily guessable HMAC secret was used for JWT signing in the `JwtProperties` class and as a default value in `application.yml` and `docker-compose.yml`.
**Risk:** If an attacker discovers or guesses this secret (which was exposed in source control), they can forge valid JWTs, completely bypassing authentication and impersonating any user, including administrators.
**Remediation:** Removed the hardcoded secret from Java code (`JwtProperties`), Spring configuration defaults (`application.yml`), and Docker configuration (`docker-compose.yml`). Enforced secret configuration via the `JWT_SECRET` environment variable for production environments. A dedicated mock secret was provided in `application-test.yml` strictly for testing purposes.
