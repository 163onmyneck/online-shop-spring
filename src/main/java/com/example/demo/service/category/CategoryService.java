package com.example.demo.service.category;

import com.example.demo.dto.category.CategoryRequestDto;
import com.example.demo.dto.category.CategoryResponseDto;
import java.util.List;

public interface CategoryService {
    CategoryResponseDto save(CategoryRequestDto requestDto);

    void deleteById(Long id);

    CategoryResponseDto getById(Long id);

    CategoryResponseDto update(Long id, CategoryRequestDto categoryRequestDto);

    List<CategoryResponseDto> findAll();
}
