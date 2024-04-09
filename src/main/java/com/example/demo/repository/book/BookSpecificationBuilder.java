package com.example.demo.repository.book;

import com.example.demo.dto.book.BookSearchParameters;
import com.example.demo.model.Book;
import com.example.demo.repository.SpecificationBuilder;
import com.example.demo.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters bookSearchParameters) {
        Specification<Book> spec = Specification.where(null);
        if (bookSearchParameters.titles() != null && bookSearchParameters.titles().length > 0) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider("title")
                .getSpecification(bookSearchParameters.titles()));
        }
        if (bookSearchParameters.authors() != null && bookSearchParameters.authors().length > 0) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider("author")
                    .getSpecification(bookSearchParameters.authors()));
        }
        return spec;
    }
}
