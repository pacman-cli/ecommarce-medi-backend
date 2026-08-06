package com.example.ecommerce.category.service;

import com.example.ecommerce.category.dto.request.CategoryFilterRequest;
import com.example.ecommerce.category.dto.request.CategoryRequest;
import com.example.ecommerce.category.dto.response.CategoryResponse;
import com.example.ecommerce.category.dto.response.CategoryTreeResponse;
import com.example.ecommerce.category.entity.Category;
import com.example.ecommerce.category.entity.CategoryStatus;
import com.example.ecommerce.category.mapper.CategoryMapper;
import com.example.ecommerce.category.repository.CategoryRepository;
import com.example.ecommerce.category.service.impl.CategoryServiceImpl;
import com.example.ecommerce.category.validator.CategoryValidator;
import com.example.ecommerce.common.dto.response.PageResponse;
import com.example.ecommerce.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private CategoryValidator categoryValidator;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category parentCategory;
    private Category childCategory;
    private CategoryRequest categoryRequest;
    private CategoryResponse categoryResponse;

    @BeforeEach
    void setUp() {
        parentCategory = Category.builder()
                .name("Electronics")
                .slug("electronics")
                .status(CategoryStatus.ACTIVE)
                .sortOrder(1)
                .build();
        parentCategory.setId(1L);

        childCategory = Category.builder()
                .name("Smartphones")
                .slug("smartphones")
                .parent(parentCategory)
                .status(CategoryStatus.ACTIVE)
                .sortOrder(1)
                .build();
        childCategory.setId(2L);

        categoryRequest = CategoryRequest.builder()
                .name("Smartphones")
                .parentId(1L)
                .description("Latest phones")
                .build();

        categoryResponse = CategoryResponse.builder()
                .id(2L)
                .name("Smartphones")
                .slug("smartphones")
                .parentId(1L)
                .status(CategoryStatus.ACTIVE)
                .build();
    }

    @Test
    @DisplayName("createCategory should validate, assign parent, auto-generate slug and save")
    void createCategory_Success() {
        doNothing().when(categoryValidator).validateForCreate(any());
        when(categoryMapper.toEntity(any())).thenReturn(childCategory);
        when(categoryRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(parentCategory));
        when(categoryRepository.save(any())).thenReturn(childCategory);
        when(categoryMapper.toResponse(any())).thenReturn(categoryResponse);

        CategoryResponse result = categoryService.createCategory(categoryRequest);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getName()).isEqualTo("Smartphones");

        verify(categoryValidator).validateForCreate(categoryRequest);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @DisplayName("getCategoryBySlug should return category response when slug exists")
    void getCategoryBySlug_Success() {
        when(categoryRepository.findBySlugAndDeletedFalse("smartphones")).thenReturn(Optional.of(childCategory));
        when(categoryMapper.toResponse(childCategory)).thenReturn(categoryResponse);

        CategoryResponse response = categoryService.getCategoryBySlug("smartphones");

        assertThat(response).isNotNull();
        assertThat(response.getSlug()).isEqualTo("smartphones");
    }

    @Test
    @DisplayName("getCategoryBySlug should throw ResourceNotFoundException when slug does not exist")
    void getCategoryBySlug_NotFound() {
        when(categoryRepository.findBySlugAndDeletedFalse("unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getCategoryBySlug("unknown"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category not found with slug: unknown");
    }

    @Test
    @DisplayName("deleteCategory should soft delete category and deactivate it")
    void deleteCategory_Success() {
        when(categoryRepository.findByIdAndDeletedFalse(2L)).thenReturn(Optional.of(childCategory));
        when(categoryRepository.save(any())).thenReturn(childCategory);

        categoryService.deleteCategory(2L);

        assertThat(childCategory.isDeleted()).isTrue();
        assertThat(childCategory.getStatus()).isEqualTo(CategoryStatus.INACTIVE);
        verify(categoryRepository).save(childCategory);
    }

    @Test
    @DisplayName("getCategoryTree should return hierarchical tree response")
    void getCategoryTree_Success() {
        CategoryTreeResponse treeNode = CategoryTreeResponse.builder()
                .id(1L)
                .name("Electronics")
                .slug("electronics")
                .children(List.of())
                .build();

        when(categoryRepository.findRootCategoriesWithChildren()).thenReturn(List.of(parentCategory));
        when(categoryMapper.toTreeResponseList(List.of(parentCategory))).thenReturn(List.of(treeNode));

        List<CategoryTreeResponse> tree = categoryService.getCategoryTree();

        assertThat(tree).hasSize(1);
        assertThat(tree.get(0).getName()).isEqualTo("Electronics");
    }

    @Test
    @DisplayName("getCategories should execute specification and return PageResponse")
    void getCategories_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> categoryPage = new PageImpl<>(List.of(parentCategory));

        when(categoryRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(categoryPage);
        when(categoryMapper.toResponse(parentCategory)).thenReturn(categoryResponse);

        CategoryFilterRequest filter = CategoryFilterRequest.builder().search("phone").build();
        PageResponse<CategoryResponse> pageResponse = categoryService.getCategories(filter, pageable);

        assertThat(pageResponse).isNotNull();
        assertThat(pageResponse.getContent()).hasSize(1);
        verify(categoryRepository).findAll(any(Specification.class), any(Pageable.class));
    }
}
