package com.example.demo.service.impl;

import com.example.demo.dto.BookDto;
import com.example.demo.dto.CreateBookRequestDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto bookDto) {
        return bookMapper.toDto(bookRepository.save(bookMapper.toModel(bookDto)));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
            .map(bookMapper::toDto)
            .toList();
    }

    @Override
    public BookDto getById(Long id) {
        return bookMapper.toDto(bookRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Can not get book by id:" + id)));
    }

    @Override
    public BookDto update(CreateBookRequestDto createBookRequestDto, Long id) {
        if (getById(id) != null) {
            throw new EntityNotFoundException("Book with this id doesn't exist. Id:" + id);
        }
        Book book = bookMapper.toModel(createBookRequestDto);
        book.setId(id);
        return bookMapper.toDto(bookRepository.save(book));

    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
