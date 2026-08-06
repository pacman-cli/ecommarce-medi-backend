package com.example.ecommerce.common.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Paginated response wrapper decoupling API consumers from Spring Data {@link Page} internals.
 *
 * @param <T> the item type
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Standard paginated response wrapper")
public class PageResponse<T> {

    @Schema(description = "List of items for the current page")
    private List<T> content;

    @Schema(description = "Current page number (0-indexed)", example = "0")
    private int page;

    @Schema(description = "Page size capacity", example = "20")
    private int size;

    @Schema(description = "Total number of elements matching query", example = "100")
    private long totalElements;

    @Schema(description = "Total number of pages", example = "5")
    private int totalPages;

    @Schema(description = "Is first page flag", example = "true")
    private boolean first;

    @Schema(description = "Is last page flag", example = "false")
    private boolean last;

    @Schema(description = "Has next page flag", example = "true")
    private boolean hasNext;

    @Schema(description = "Has previous page flag", example = "false")
    private boolean hasPrevious;

    @Schema(description = "Optional page metadata summary")
    private Map<String, Object> metadata;

    public List<T> getItems() {
        return content;
    }

    public static <S, T> PageResponse<T> from(Page<S> page, Function<S, T> mapper) {
        return PageResponse.<T>builder()
                .content(page.getContent().stream().map(mapper).toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }

    public static <S, T> PageResponse<T> from(Page<S> page, Function<S, T> mapper, Map<String, Object> metadata) {
        PageResponse<T> response = from(page, mapper);
        response.setMetadata(metadata);
        return response;
    }
}
