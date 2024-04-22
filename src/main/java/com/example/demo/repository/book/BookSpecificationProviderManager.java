package com.example.demo.repository.book;

import com.example.demo.exception.ProviderNotFoundException;
import com.example.demo.model.Book;
import com.example.demo.repository.SpecificationProvider;
import com.example.demo.repository.SpecificationProviderManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> phoneSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return phoneSpecificationProviders.stream()
            .filter(p -> p.getKey().equals(key))
            .findFirst()
            .orElseThrow(() ->
                new ProviderNotFoundException("Can not find specification provider for key: "
                    + key));
    }
}
