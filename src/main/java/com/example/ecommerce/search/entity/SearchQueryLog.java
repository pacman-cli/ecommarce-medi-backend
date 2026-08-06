package com.example.ecommerce.search.entity;

import com.example.ecommerce.entity.BaseEntity;
import com.example.ecommerce.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Entity logging user search query terms, hit counts, and execution timestamps.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "search_query_logs",
        indexes = {
                @Index(name = "idx_search_query_term", columnList = "query_term"),
                @Index(name = "idx_search_user_id", columnList = "user_id"),
                @Index(name = "idx_search_created_at", columnList = "created_at")
        }
)
public class SearchQueryLog extends BaseEntity {

    @Column(name = "query_term", nullable = false, length = 150)
    private String queryTerm;

    @Column(name = "normalized_term", nullable = false, length = 150)
    private String normalizedTerm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "result_count", nullable = false)
    @Builder.Default
    private Long resultCount = 0L;

    @Column(name = "search_count", nullable = false)
    @Builder.Default
    private Long searchCount = 1L;

    @Column(name = "last_searched_at", nullable = false)
    @Builder.Default
    private Instant lastSearchedAt = Instant.now();
}
