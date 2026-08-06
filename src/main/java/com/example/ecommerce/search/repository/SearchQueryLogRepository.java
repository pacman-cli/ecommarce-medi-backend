package com.example.ecommerce.search.repository;

import com.example.ecommerce.search.entity.SearchQueryLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link SearchQueryLog} tracking records.
 */
@Repository
public interface SearchQueryLogRepository extends JpaRepository<SearchQueryLog, Long> {

    Optional<SearchQueryLog> findByNormalizedTermAndUserId(String normalizedTerm, Long userId);

    Optional<SearchQueryLog> findFirstByNormalizedTermOrderBySearchCountDesc(String normalizedTerm);

    @Query("SELECT s.queryTerm, SUM(s.searchCount) as totalHits " +
           "FROM SearchQueryLog s " +
           "WHERE s.lastSearchedAt >= :since " +
           "GROUP BY s.queryTerm " +
           "ORDER BY totalHits DESC")
    List<Object[]> findTrendingSearchTerms(@Param("since") Instant since, Pageable pageable);

    @Query("SELECT DISTINCT s.queryTerm " +
           "FROM SearchQueryLog s " +
           "WHERE LOWER(s.queryTerm) LIKE LOWER(CONCAT(:prefix, '%')) " +
           "GROUP BY s.queryTerm " +
           "ORDER BY SUM(s.searchCount) DESC")
    List<String> findAutocompleteKeywords(@Param("prefix") String prefix, Pageable pageable);

    @Query("SELECT s.queryTerm, s.lastSearchedAt " +
           "FROM SearchQueryLog s " +
           "WHERE s.user.id = :userId " +
           "ORDER BY s.lastSearchedAt DESC")
    List<Object[]> findRecentSearchesByUserId(@Param("userId") Long userId, Pageable pageable);

    void deleteByUserId(Long userId);
}
