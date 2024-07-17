package com.example.demo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {

    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired DataSource dataSource,
                          @Autowired WebApplicationContext webApplicationContext)
            throws SQLException {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                                 .apply(springSecurity())
                                 .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection, new ClassPathResource(
                    "database/books/add-books-to-db.sql"));
        }
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection, new ClassPathResource(
                    "database/books/remove-all-books.sql"));
        }
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("method returns all available books")
    @Transactional
    void getAll_ValidRequest_ShouldReturnAllAvailableBooks() throws Exception {
        List<BookDto> expected = new ArrayList<>();
        expected.add(new BookDto().setId(1L)
                                  .setTitle("Book Title1")
                                  .setAuthor("Author Name1")
                                  .setIsbn("1234567890123")
                                  .setPrice(BigDecimal.valueOf(23.99))
                                  .setDescription("Book description1")
                                  .setCoverImage("image_url1")
                                  .setCategories(new HashSet<>()));
        expected.add(new BookDto().setId(2L)
                                  .setTitle("Book Title2")
                                  .setAuthor("Author Name2")
                                  .setIsbn("12345678901232")
                                  .setPrice(BigDecimal.valueOf(45.99))
                                  .setDescription("Book description2")
                                  .setCoverImage("image_url2")
                                  .setCategories(new HashSet<>()));

        MvcResult result = mockMvc.perform(get("/books"))
                                  .andExpect(status().isOk())
                                  .andReturn();
        BookDto[] actual = objectMapper.readValue(
            result.getResponse()
                  .getContentAsString(), BookDto[].class);
        Assertions.assertEquals(expected.size(), actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual)
                                                .toList());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("method returns book by id")
    @Transactional
    void getBookById_ValidId_ShouldReturnBookDto() throws Exception {
        BookDto expected = new BookDto().setId(2L)
                                        .setTitle("Book Title2")
                                        .setAuthor("Author Name2")
                                        .setIsbn("12345678901232")
                                        .setPrice(BigDecimal.valueOf(45.99))
                                        .setDescription("Book description2")
                                        .setCoverImage("image_url2")
                                        .setCategories(new HashSet<>());

        MvcResult result = mockMvc.perform(get("/books/2"))
                                  .andExpect(status().isOk())
                                  .andReturn();

        BookDto actual = objectMapper.readValue(
                            result.getResponse()
                                    .getContentAsString(), BookDto.class);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("method updates existing book by id")
    void update_ValidDto_ShouldReturnUpdatedBookDto() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto().setAuthor(
                                                                        "Expected Author")
                                                                    .setTitle(
                                                                        "Expected Title")
                                                                    .setDescription(
                                                                        "Expected Description")
                                                                    .setIsbn(
                                                                        "3432435345")
                                                                    .setPrice(
                                                                        BigDecimal.valueOf(
                                                                            45.99))
                                                                    .setCoverImage(
                                                                        "Expected coverImage");

        BookDto expected = new BookDto().setId(2L)
                                        .setAuthor("Expected Author")
                                        .setTitle("Expected Title")
                                        .setDescription("Expected Description")
                                        .setIsbn("3432435345")
                                        .setPrice(BigDecimal.valueOf(45.99))
                                        .setCoverImage("Expected coverImage");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(put("/books/2").content(jsonRequest)
                                                          .contentType(
                                                              MediaType.APPLICATION_JSON))
                                  .andExpect(status().isOk())
                                  .andReturn();
        BookDto actual = objectMapper.readValue(
                            result.getResponse()
                                  .getContentAsString(), BookDto.class);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("method saves book to db and return its dto")
    void createBook_ValidFields_ShouldReturnBookDto() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto().setAuthor(
                                                                        "Expected Author")
                                                                    .setTitle(
                                                                        "Expected Title")
                                                                    .setDescription(
                                                                        "Expected Description")
                                                                    .setIsbn(
                                                                        "3432435345")
                                                                    .setPrice(
                                                                        BigDecimal.valueOf(
                                                                            45.99))
                                                                    .setCoverImage(
                                                                        "Expected coverImage");

        BookDto expected = new BookDto().setId(3L)
                                        .setAuthor("Expected Author")
                                        .setTitle("Expected Title")
                                        .setDescription("Expected Description")
                                        .setIsbn("3432435345")
                                        .setPrice(BigDecimal.valueOf(45.99))
                                        .setCoverImage("Expected coverImage");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/books").content(jsonRequest)
                                                         .contentType(
                                                             MediaType.APPLICATION_JSON))
                                  .andExpect(status().isCreated())
                                  .andReturn();

        BookDto actual = objectMapper.readValue(
                        result.getResponse()
                                .getContentAsString(), BookDto.class);
        assertThat(actual).isEqualTo(expected);
    }
}
