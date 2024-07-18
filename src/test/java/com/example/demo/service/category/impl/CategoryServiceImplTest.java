package com.example.demo.service.category.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.example.demo.dto.category.CategoryRequestDto;
import com.example.demo.dto.category.CategoryResponseDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.model.Category;
import com.example.demo.repository.category.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Saving category with correct fields")
    void saveCategory_ShouldReturnCategoryResponseDto() {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setName("categoryName");
        categoryRequestDto.setDescription("description");

        Category category = new Category();
        category.setId(1L);
        category.setName(categoryRequestDto.getName());
        category.setDescription(categoryRequestDto.getDescription());

        CategoryResponseDto expected = new CategoryResponseDto();
        expected.setName(categoryRequestDto.getName());
        expected.setDescription(categoryRequestDto.getDescription());

        when(categoryMapper.toModel(categoryRequestDto)).thenReturn(category);
        when(categoryRepository.save(Mockito.any())).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expected);

        CategoryResponseDto actual = categoryService.save(categoryRequestDto);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("getting category by valid id")
    void getById_ValidId_ShouldReturnCategoryResponseDto() {
        Category category = new Category();
        category.setId(1L);
        category.setName("categoryName");
        category.setDescription("description");

        CategoryResponseDto expected = new CategoryResponseDto();
        expected.setName(category.getName());
        expected.setDescription(category.getDescription());

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expected);

        CategoryResponseDto actual = categoryService.getById(category.getId());

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("getting category by invalid id")
    void getById_InvalidId_ShouldThrowException() {
        Long invalidId = 1L;

        when(categoryRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getById(invalidId))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Can not find category by id: "
                                          + invalidId);
    }

    @Test
    @DisplayName("method must update existing entity by"
                 + "id and return dto of this category")
    void update_ValidCategory_ShouldReturnCategoryResponseDto() {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setName("expected name");
        categoryRequestDto.setDescription("expected description");

        Category category = new Category();
        category.setId(1L);
        category.setName("old name");
        category.setDescription("old description");

        CategoryResponseDto expected = new CategoryResponseDto();
        expected.setName("old name");
        expected.setDescription("old description");

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expected);
        when(categoryMapper.toModel(categoryRequestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);

        CategoryResponseDto actual = categoryService.update(category.getId(),
                                                            categoryRequestDto);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Finding all available categories")
    void findAll_AvailableCategories_ShouldReturnAllCategoryResponseDtos() {
        Category category = new Category();
        category.setId(1L);
        category.setName("categoryName");
        category.setDescription("description");

        CategoryResponseDto expected = new CategoryResponseDto();
        expected.setName(category.getName());
        expected.setDescription(category.getDescription());

        List<Category> categoryList = List.of(category);

        when(categoryRepository.findAll()).thenReturn(categoryList);
        when(categoryMapper.toDto(category)).thenReturn(expected);

        List<CategoryResponseDto> actualList = categoryService.findAll();

        assertThat(actualList).hasSize(1);
        assertThat(actualList.get(0)).isEqualTo(expected);
    }
}
