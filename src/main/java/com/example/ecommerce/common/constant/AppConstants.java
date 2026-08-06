package com.example.ecommerce.common.constant;

/**
 * Application-wide constants used across layers.
 *
 * <p>Centralising constants avoids magic numbers and guarantees consistent
 * pagination defaults, Redis key namespaces and cache configuration.</p>
 */
public final class AppConstants {

    /* ------------------------------ Pagination ------------------------------ */

    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIRECTION = "asc";
    public static final int MAX_PAGE_SIZE = 100;
    public static final String SORT_DIRECTION_ASC = "asc";
    public static final String SORT_DIRECTION_DESC = "desc";

    /* -------------------------------- Redis --------------------------------- */

    public static final String REDIS_ACCESS_TOKEN_BLACKLIST_PREFIX = ":blacklist:token:";
    public static final String REDIS_REFRESH_TOKEN_PREFIX = ":refresh:user:";
    public static final String REDIS_LOGIN_ATTEMPTS_PREFIX = ":login:attempts:";
    public static final String REDIS_LOGIN_BLOCKED_PREFIX = ":login:blocked:";
    public static final long REDIS_CACHE_TTL_MINUTES = 10;

    /* --------------------------------- Audit -------------------------------- */

    public static final String SYSTEM_AUDITOR = "system";

    /* ------------------------------- Headers -------------------------------- */

    public static final String TRACE_ID_HEADER = "X-Trace-Id";
    public static final String TRACE_ID_MDC_KEY = "traceId";

    private AppConstants() {
    }
}
