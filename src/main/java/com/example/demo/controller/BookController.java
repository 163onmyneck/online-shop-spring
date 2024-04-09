package com.example.demo.controller;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.BookSearchParameters;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.example.demo.service.book.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book management", description = "Endpoints for mapping books")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/books")
public class BookController {

    private final BookService bookService;

    @GetMapping
    @Operation(summary = "get all books", description = "get list of available books")
    public List<BookDto> getAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "get book by id", description = "get available book bu id")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "create a book", description = "create a new book")
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto bookDto) {
        return bookService.save(bookDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "update a book", description = "update existing book")
    public BookDto updateBook(@RequestBody CreateBookRequestDto bookRequestDto,
                           @PathVariable Long id) {
        return bookService.update(bookRequestDto, id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete book by id", description = "delete existing book by id")
    public void deleteById(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @GetMapping("/search")
    @Operation(summary = "search books", description = "search a list of books by input parameters")
    public List<BookDto> search(BookSearchParameters bookSearchParameters, Pageable pageable) {
        return bookService.search(bookSearchParameters, pageable);
    }
}
