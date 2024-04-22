package com.example.demo.service.book.impl;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.BookDtoWithoutCategoryIds;
import com.example.demo.dto.book.BookSearchParameters;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.repository.book.BookSpecificationBuilder;
import com.example.demo.service.book.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder specificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto bookDto) {
        return bookMapper.toDto(bookRepository.save(bookMapper.toModel(bookDto)));
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
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
        if (getById(id) == null) {
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

    @Override
    public List<BookDto> search(BookSearchParameters bookSearchParameters, Pageable pageable) {
        Specification<Book> build = specificationBuilder.build(bookSearchParameters);
        return bookRepository.findAll(build, pageable)
            .stream()
            .map(bookMapper::toDto)
            .toList();
    }

    @Override
    public List<BookDtoWithoutCategoryIds> getAllBooksByCategoryId(Long categoryId) {
        return bookRepository.findByCategoryId(categoryId).stream()
            .map(bookMapper::toDtoWithoutCategories)
            .toList();
    }
}
