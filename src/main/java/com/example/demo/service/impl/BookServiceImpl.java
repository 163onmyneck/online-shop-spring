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
    public void update(CreateBookRequestDto createBookRequestDto, Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Can not get book by id:" + id));
        setFields(createBookRequestDto, book);
        bookRepository.save(book);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    private void setFields(CreateBookRequestDto createBookRequestDto, Book book) {
        if (createBookRequestDto.getTitle() != null) {
            book.setTitle(createBookRequestDto.getTitle());
        }
        if (createBookRequestDto.getAuthor() != null) {
            book.setAuthor(createBookRequestDto.getAuthor());
        }
        if (createBookRequestDto.getIsbn() != null) {
            book.setIsbn(createBookRequestDto.getIsbn());
        }
        if (createBookRequestDto.getPrice() != null) {
            book.setPrice(createBookRequestDto.getPrice());
        }
        if (createBookRequestDto.getDescription() != null) {
            book.setDescription(createBookRequestDto.getDescription());
        }
        if (createBookRequestDto.getCoverImage() != null) {
            book.setCoverImage(createBookRequestDto.getCoverImage());
        }
    }
}
