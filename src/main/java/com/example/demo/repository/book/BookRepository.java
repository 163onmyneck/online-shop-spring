package com.example.demo.repository.book;

import com.example.demo.model.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @Query("FROM Book b JOIN b.categories c WHERE c.id = :categoryId")
    List<Book> findByCategoryId(@Param("categoryId") Long categoryId);
}
