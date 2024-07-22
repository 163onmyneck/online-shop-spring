package com.example.demo.service.category.impl;

import com.example.demo.dto.category.CategoryRequestDto;
import com.example.demo.dto.category.CategoryResponseDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.repository.category.CategoryRepository;
import com.example.demo.service.category.CategoryService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponseDto save(CategoryRequestDto requestDto) {
        return categoryMapper.toDto(categoryRepository.save(categoryMapper.toModel(requestDto)));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryResponseDto getById(Long id) {
        return categoryMapper.toDto(categoryRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Can not find category by id: " + id)));
    }

    @Override
    @Transactional
    public CategoryResponseDto update(Long id, CategoryRequestDto categoryRequestDto) {
        CategoryResponseDto categoryToUpdate = getById(id);
        categoryToUpdate.setName(categoryRequestDto.getName());
        categoryToUpdate.setDescription(categoryRequestDto.getDescription());
        categoryRepository.save(categoryMapper.toModel(categoryRequestDto));
        return categoryToUpdate;
    }

    @Override
    public List<CategoryResponseDto> findAll() {
        return categoryRepository.findAll().stream()
            .map(categoryMapper::toDto)
            .toList();
    }
}
