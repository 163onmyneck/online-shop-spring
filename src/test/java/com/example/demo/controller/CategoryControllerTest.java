package com.example.demo.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.dto.category.CategoryRequestDto;
import com.example.demo.dto.category.CategoryResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {

    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                                 .apply(springSecurity())
                                 .build();
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                                         new ClassPathResource(
                                             "database/books/remove-all-books.sql"));
        }
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @Transactional
    @DisplayName("method saves category to db and returns dto of saved category")
    void createCategory() throws Exception {
        CategoryRequestDto requestDto = new CategoryRequestDto().setName(
                                                                    "expected category")
                                                                .setDescription(
                                                                    "expected description");

        CategoryResponseDto expected = new CategoryResponseDto().setId(3L)
                                                                .setName(
                                                                    "expected category")
                                                                .setDescription(
                                                                    "expected description");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(
                                                                     "/categories/create")
                                                                 .content(jsonRequest)
                                                                 .contentType(
                                                                     MediaType.APPLICATION_JSON))
                                  .andExpect(status().isOk())
                                  .andReturn();

        CategoryResponseDto actual = objectMapper.readValue(result.getResponse()
                                                                  .getContentAsString(),
                                                            CategoryResponseDto.class);

        EqualsBuilder.reflectionEquals(actual,
                                       expected,
                                       "id");
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @Transactional
    @Sql(scripts = "classpath:database/category/add-2-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("method must return 2 categories")
    void getAll() throws Exception {
        MvcResult result = mockMvc.perform(
                                      MockMvcRequestBuilders.get("/categories/find-all")
                                                            .contentType(
                                                                MediaType.APPLICATION_JSON))
                                  .andExpect(status().isOk())
                                  .andReturn();

        CategoryResponseDto[] actualCategories = objectMapper.readValue(
            result.getResponse()
                  .getContentAsString(),
            CategoryResponseDto[].class);

        CategoryResponseDto firstExpected = new CategoryResponseDto().setId(1L)
                                                                     .setName("Adventure")
                                                                     .setDescription(
                                                                         "adventure category");

        CategoryResponseDto secondExpected = new CategoryResponseDto().setId(2L)
                                                                      .setName("Romance")
                                                                      .setDescription(
                                                                          "romantic category");

        assertThat(actualCategories).hasSize(2);
        assertThat(actualCategories).containsExactly(firstExpected,
                                                     secondExpected);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @Transactional
    @Sql(scripts = "classpath:database/category/add-2-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("method must return category dto by id")
    void getCategoryById() throws Exception {
        CategoryResponseDto expected = new CategoryResponseDto().setId(1L)
                                                                .setName("Adventure")
                                                                .setDescription(
                                                                    "adventure category");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/categories/1")
                                                                 .contentType(
                                                                     MediaType.APPLICATION_JSON))
                                  .andExpect(status().isOk())
                                  .andReturn();

        CategoryResponseDto actual = objectMapper.readValue(result.getResponse()
                                                                  .getContentAsString(),
                                                            CategoryResponseDto.class);

        assertThat(actual).isEqualTo(expected);
    }
}
