# PharmaCare Backend — Operations Runbook

Production host: **Render** (Docker service, `render.yaml`). Database: **dedicated Postgres**
(the keep of the storefront DB created from `supabase/schema_master.sql` — different schema).
Redis is **optional** (default off; in-memory fallback for JWT blacklist + login lockout).

## Architecture (startup path)

```text
java -jar app.jar            (Dockerfile ENTRYPOINT, non-root user `ecommerce`)
  -> SpringApplication.run(EcommerceApplication)
     -> prod profile (application-prod.yml)
        -> DataSource (HikariCP)  -> dedicated Postgres  (ddl-auto: update creates the 36 tables)
        -> Hibernate/JPA          -> 36 backend entities, ddl-auto: update
        -> Spring Security        -> JWT filter chain (stateless)
        -> Redis optional         -> in-memory fallback (token blacklist / login lockout)
     -> Embedded Tomcat on :8080
     -> Actuator  /actuator/health == 200 {"status":"UP"}
```

`ddl-auto` is `update` by default; switch to `validate` (via `JPA_DDL_AUTO`) only after the
schema is version-controlled (add Flyway/Liquibase before doing so).

## Environment / Secrets

| Render secret/variable | Purpose | Source |
|---|---|---|
| `SPRING_DATASOURCE_URL` (or `DB_URL`) | Dedicated backend Postgres JDBC URL | you |
| `SPRING_DATASOURCE_USERNAME` (or `DB_USERNAME`) | DB role | you |
| `SPRING_DATASOURCE_PASSWORD` (or `DB_PASSWORD`) | **sync:false** — must be set manually | you |
| `JWT_SECRET` | HMAC signing key; Render `generateValue` | Render |
| `CORS_ALLOWED_ORIGINS` | Frontend URLs | Render |
| `REDIS_ENABLED=false` | Redis optional; keep false until Redis added | Render |
| `STORAGE_BASE_URL` | `${RENDER_EXTERNAL_URL}` (already includes `https://`) | auto |

Secrets that must **never** be committed: `DB_PASSWORD`, `JWT_SECRET`, `MAIL_PASSWORD`,
`REDIS_PASSWORD`. Use Render secret manager / GitHub secrets, never the repo.

## Deploy

1. Merge to `main`. CI (`ci.yml`) runs **tests + Trivy image scan**; a CRITICAL/HIGH finding fails the build.
2. CI pushes a pinned `sha-<commit>` image to GHCR (never `latest` in prod).
3. CI passes → `deploy.yml` runs, gated by the **`production` GitHub environment** (requires human approval).
4. It POSTs the Render Deploy Hook, then smoke-checks `GET /actuator/health` until `{"status":"UP"}`.

Manual redeploy: trigger `deploy.yml` `workflow_dispatch`.

## Verify (post-deploy)

```bash
curl -sf https://<service>.onrender.com/actuator/health          # {"status":"UP"}
curl -sf https://<service>.onrender.com/actuator/info
curl -s  http://<api-host>:8080/v3/api-docs | head -c 120         # swagger present
# auth smoke
curl -s -X POST https://<service>.onrender.com/api/v1/auth/register \
  -H 'Content-Type: application/json' \
  -d '{"email":"smoke@example.com","password":"Passw0rd!","firstName":"A","lastName":"B"}'
```

## Rollback

Two options, choose based on how far the bad deploy has spread.

**Option A — Render Rollback (fast, recommended for bad release):**
1. Render dashboard → **Deploys**.
2. Locate the last **successful** deploy and click **Rollback**.
3. Watch logs; re-run the health probe above.

**Option B — Git revert (durable fix):**
```bash
git checkout main && git pull
git revert HEAD                      # revert the bad change
git push origin main                 # CI -> approved deploy of the fixed commit
```

Rollback must be no more than 5 minutes to call. Document what you rolled back in the
deployment log / PR.

## Monitoring & Alerts

- **Readiness/liveness:** `/actuator/health` (OK = probe 200 UP). Render probes this and
  marks Live.
- **Metrics:** `/actuator/metrics` + `/actuator/info` exposed (add `/prometheus` by adding
  `micrometer-registry-prometheus` if you want a Grafana dashboard).
- **Alerts (suggested):** Render uptime/15-min probe; log keyword `[ERROR]` and `APPLICATION FAILED`;
  DB connection-pool exhaustion (`HikariPool-1 — Exception during pool initialization`).

## Incident Response

| Severity | Impact | Response window |
|---|---|---|
| SEV1 | Total outage / payment / auth down | Immediate |
| SEV2 | Major degradation (high error rate, latency) | 15 min |
| SEV3 | Minor feature broken | 1 hour |

Basic runbook for "backend unhealthy":
1. **Triage** — are logs showing `APPLICATION FAILED`, `Schema-validation`, `No qualifying bean`, `HikariPool` errors?
2. **Check deps** — is the DB reachable? (`pg_isready` against the dedicated Postgres). Is Redis expected? (`REDIS_ENABLED`).
3. **Rollback** (Option A above) if it started after the last deploy.
4. **Verify** health returns UP; confirm auth works.
5. **Postmortem** — even for small ones, capture timeline + root cause + action items.

## CI/CD hygiene

- Image is pinned by SHA in each CI job (never `latest` in production). 
- Trivy scans both the source tree (SARIF → Security tab) and the built image (gate).
- Dependabot raises weekly PRs for Maven/Docker/GitHub-actions.
- Secrets live only in GitHub/Render secrets — never commit `.env`.

## Known long-term improvements (ordered)
1. Backup + restore procedure for the Postgres (DB snapshots / point-in-time).
2. Add a proper migration tool (Flyway) and switch `ddl-auto` to `validate`.
3. Add Prometheus endpoint + alerting for the 4 core signals (uptime, error, latency, RSS).
4. Add Redis when multi-instance / shared sessions are needed.