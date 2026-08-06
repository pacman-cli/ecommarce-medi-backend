package com.example.ecommerce.category.validator;

import com.example.ecommerce.category.entity.Category;
import com.example.ecommerce.category.repository.CategoryRepository;
import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.exception.ConflictException;
import com.example.ecommerce.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryValidatorTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryValidator categoryValidator;

    private Category grandParent;
    private Category parent;

    @BeforeEach
    void setUp() {
        grandParent = Category.builder().name("Electronics").slug("electronics").build();
        grandParent.setId(10L);

        parent = Category.builder().name("Mobiles").slug("mobiles").parent(grandParent).build();
        parent.setId(20L);
    }

    @Test
    @DisplayName("validateParentCategory should fail when category is set as its own parent")
    void validateParentCategory_SelfParent_ThrowsBadRequest() {
        assertThatThrownBy(() -> categoryValidator.validateParentCategory(5L, 5L))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("A category cannot be assigned as its own parent");
    }

    @Test
    @DisplayName("validateParentCategory should fail when parent is not found")
    void validateParentCategory_ParentNotFound_ThrowsResourceNotFound() {
        when(categoryRepository.findByIdAndDeletedFalse(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryValidator.validateParentCategory(99L, 1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Parent category not found with ID: 99");
    }

    @Test
    @DisplayName("validateParentCategory should detect circular references in hierarchy")
    void validateParentCategory_CircularReference_ThrowsBadRequest() {
        // Attempting to set grandParent's parent to 'parent' (which is a child of grandParent)
        when(categoryRepository.findByIdAndDeletedFalse(20L)).thenReturn(Optional.of(parent));

        assertThatThrownBy(() -> categoryValidator.validateParentCategory(20L, 10L))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Circular reference detected");
    }

    @Test
    @DisplayName("validateNameUniqueness should throw ConflictException if duplicate name exists")
    void validateNameUniqueness_Duplicate_ThrowsConflict() {
        when(categoryRepository.existsByNameIgnoreCase("Electronics")).thenReturn(true);

        assertThatThrownBy(() -> categoryValidator.validateNameUniqueness("Electronics", null))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Category with name 'Electronics' already exists");
    }

    @Test
    @DisplayName("validateSlugUniqueness should throw ConflictException if duplicate slug exists")
    void validateSlugUniqueness_Duplicate_ThrowsConflict() {
        when(categoryRepository.existsBySlugIgnoreCase("electronics")).thenReturn(true);

        assertThatThrownBy(() -> categoryValidator.validateSlugUniqueness("electronics", null))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Category with slug 'electronics' already exists");
    }

    @Test
    @DisplayName("validateParentCategory should pass for valid hierarchy")
    void validateParentCategory_Valid_Success() {
        when(categoryRepository.findByIdAndDeletedFalse(10L)).thenReturn(Optional.of(grandParent));

        assertThatCode(() -> categoryValidator.validateParentCategory(10L, 30L))
                .doesNotThrowAnyException();
    }
}
