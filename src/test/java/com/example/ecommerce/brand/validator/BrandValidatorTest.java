package com.example.ecommerce.brand.validator;

import com.example.ecommerce.brand.repository.BrandRepository;
import com.example.ecommerce.exception.ConflictException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BrandValidatorTest {

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandValidator brandValidator;

    @Test
    @DisplayName("validateNameUniqueness should throw ConflictException if duplicate name exists")
    void validateNameUniqueness_Duplicate_ThrowsConflict() {
        when(brandRepository.existsByNameIgnoreCase("Apple")).thenReturn(true);

        assertThatThrownBy(() -> brandValidator.validateNameUniqueness("Apple", null))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Brand with name 'Apple' already exists");
    }

    @Test
    @DisplayName("validateSlugUniqueness should throw ConflictException if duplicate slug exists")
    void validateSlugUniqueness_Duplicate_ThrowsConflict() {
        when(brandRepository.existsBySlugIgnoreCase("apple")).thenReturn(true);

        assertThatThrownBy(() -> brandValidator.validateSlugUniqueness("apple", null))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Brand with slug 'apple' already exists");
    }

    @Test
    @DisplayName("validateNameUniqueness should pass when name is unique")
    void validateNameUniqueness_Unique_Success() {
        when(brandRepository.existsByNameIgnoreCase("Samsung")).thenReturn(false);

        assertThatCode(() -> brandValidator.validateNameUniqueness("Samsung", null))
                .doesNotThrowAnyException();
    }
}
