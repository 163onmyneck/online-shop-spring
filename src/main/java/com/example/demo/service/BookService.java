package com.example.demo.service;

import com.example.demo.dto.BookDto;
import com.example.demo.dto.BookSearchParameters;
import com.example.demo.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto bookDto);

    List<BookDto> findAll();

    BookDto getById(Long id);

    BookDto update(CreateBookRequestDto createBookRequestDto, Long id);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParameters bookSearchParameters);
}
