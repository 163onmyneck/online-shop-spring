package com.example.demo.service.book.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.BookDtoWithoutCategoryIds;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.repository.book.BookRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
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

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Verify the correct fields were returned when book exists")
    void getBookById_WithValidBookId_ShouldReturnUser() {
        Long bookId = 33L;
        Book book = new Book();
        book.setId(bookId);
        book.setAuthor("Taras Shevchenko");
        book.setTitle("Kobzar");
        book.setIsbn("12321343243214314");
        book.setCoverImage("coverImage");
        book.setDescription("description");
        book.setPrice(BigDecimal.TEN);

        BookDto bookDto = new BookDto();
        bookDto.setId(bookId);
        bookDto.setAuthor(book.getAuthor());
        bookDto.setTitle(book.getTitle());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setCoverImage(book.getCoverImage());
        bookDto.setDescription(book.getDescription());
        bookDto.setPrice(book.getPrice());

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto bookServiceById = bookService.getById(bookId);

        assertThat(bookServiceById).isEqualTo(bookDto);
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Using wrong id, method must throw an exception")
    void getById_NonExistingId_ShouldReturnException() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class,
                                () -> bookService.getById(anyLong()));
    }

    @Test
    @DisplayName("Saving book with correct fields in a right way")
    void save_NormalBook_ShouldReturnBook() {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setAuthor("Taras Shevchenko");
        createBookRequestDto.setTitle("Kobzar");
        createBookRequestDto.setIsbn("12321343243214314");
        createBookRequestDto.setDescription("description");
        createBookRequestDto.setPrice(BigDecimal.TEN);
        createBookRequestDto.setCoverImage("coverImage");

        Book book = new Book();
        book.setId(1L);
        book.setAuthor(createBookRequestDto.getAuthor());
        book.setTitle(createBookRequestDto.getTitle());
        book.setIsbn(createBookRequestDto.getIsbn());
        book.setCoverImage(createBookRequestDto.getCoverImage());
        book.setDescription(createBookRequestDto.getDescription());
        book.setPrice(createBookRequestDto.getPrice());

        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setAuthor(createBookRequestDto.getAuthor());
        bookDto.setTitle(createBookRequestDto.getTitle());
        bookDto.setIsbn(createBookRequestDto.getIsbn());
        bookDto.setCoverImage(createBookRequestDto.getCoverImage());
        bookDto.setDescription(createBookRequestDto.getDescription());
        bookDto.setPrice(createBookRequestDto.getPrice());

        when(bookMapper.toModel(createBookRequestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto bookServiceById = bookService.save(createBookRequestDto);

        assertThat(bookServiceById).isEqualTo(bookDto);
    }

    @Test
    @DisplayName("Updating book with correct id")
    void update_WithValidId_ShouldReturnUpdatedBook() {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setAuthor("Updated Author");
        createBookRequestDto.setTitle("Updated Title");
        createBookRequestDto.setIsbn("6788678678");
        createBookRequestDto.setDescription("Updated description");
        createBookRequestDto.setPrice(BigDecimal.ZERO);
        createBookRequestDto.setCoverImage("Updated coverImage");

        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Taras Shevchenko");
        book.setTitle("Kobzar");
        book.setIsbn("12321343243214314");
        book.setCoverImage("coverImage");
        book.setDescription("description");
        book.setPrice(BigDecimal.TEN);

        Book updatedBook = new Book();
        updatedBook.setId(book.getId());
        updatedBook.setAuthor(createBookRequestDto.getAuthor());
        updatedBook.setTitle(createBookRequestDto.getTitle());
        updatedBook.setIsbn(createBookRequestDto.getIsbn());
        updatedBook.setCoverImage(createBookRequestDto.getCoverImage());
        updatedBook.setDescription(createBookRequestDto.getDescription());
        updatedBook.setPrice(createBookRequestDto.getPrice());

        BookDto updatedBookDto = new BookDto();
        updatedBookDto.setId(updatedBook.getId());
        updatedBookDto.setAuthor(updatedBook.getAuthor());
        updatedBookDto.setTitle(updatedBook.getTitle());
        updatedBookDto.setIsbn(updatedBook.getIsbn());
        updatedBookDto.setCoverImage(updatedBook.getCoverImage());
        updatedBookDto.setDescription(updatedBook.getDescription());
        updatedBookDto.setPrice(updatedBook.getPrice());

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(new BookDto());
        when(bookMapper.toModel(createBookRequestDto)).thenReturn(updatedBook);
        when(bookRepository.save(updatedBook)).thenReturn(updatedBook);
        when(bookMapper.toDto(updatedBook)).thenReturn(updatedBookDto);

        BookDto result = bookService.update(createBookRequestDto, book.getId());

        assertThat(result).isEqualTo(updatedBookDto);
    }

    @Test
    @DisplayName("Trying to update entity with invalid id")
    void update_InvalidId_ShouldThrowException() {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setAuthor("Updated Author");
        createBookRequestDto.setTitle("Updated Title");
        createBookRequestDto.setIsbn("6788678678");
        createBookRequestDto.setDescription("Updated description");
        createBookRequestDto.setPrice(BigDecimal.ZERO);
        createBookRequestDto.setCoverImage("Updated coverImage");

        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.update(createBookRequestDto, 1L))
                                            .isInstanceOf(EntityNotFoundException.class)
                                            .hasMessageContaining("Can not get book by id:" + 1L);
    }

    @Test
    @DisplayName("method returns all available books without category ids")
    void getAllBooksByCategoryId_ValidPageable_ReturnsAllBooksWithoutCategoryIds() {
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Taras Shevchenko");
        book.setTitle("Kobzar");
        book.setIsbn("12321343243214314");
        book.setCoverImage("coverImage");
        book.setDescription("description");
        book.setPrice(BigDecimal.TEN);

        BookDtoWithoutCategoryIds bookDto = new BookDtoWithoutCategoryIds();
        bookDto.setId(book.getId());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setTitle(book.getTitle());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setCoverImage(book.getCoverImage());
        bookDto.setDescription(book.getDescription());
        bookDto.setPrice(book.getPrice());

        List<Book> books = List.of(book);

        when(bookRepository.findByCategoryId(anyLong())).thenReturn(books);
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(bookDto);

        List<BookDtoWithoutCategoryIds> booksWithoutCategoryIds =
                                bookService.getAllBooksByCategoryId(anyLong());

        assertThat(booksWithoutCategoryIds.get(0)).isEqualTo(bookDto);
    }

    @Test
    @DisplayName("Getting all available books")
    void findAll_ValidPageable_ReturnsAllAvailableBooks() {
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Taras Shevchenko");
        book.setTitle("Kobzar");
        book.setIsbn("12321343243214314");
        book.setCoverImage("coverImage");
        book.setDescription("description");
        book.setPrice(BigDecimal.TEN);

        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setTitle(book.getTitle());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setCoverImage(book.getCoverImage());
        bookDto.setDescription(book.getDescription());
        bookDto.setPrice(book.getPrice());

        List<Book> books = List.of(book);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> bookDtos = bookService.findAll(pageable);

        assertThat(bookDtos.get(0)).isEqualTo(bookDto);
        assertThat(bookDtos).hasSize(books.size());
    }
}
