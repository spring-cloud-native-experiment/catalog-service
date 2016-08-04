package com.example.author.service;

import com.example.author.domain.Author;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Service
public class AuthorService {

    private Map<Long, Author> inMemoryCache = new ConcurrentHashMap<>();

    {
        Stream.of(
                new Author(1L, "Robert C. Martin")
        ).forEach(author -> inMemoryCache.put(author.getId(), author));

    }

    public Stream<Author> findAllAuthors() {
        return Stream.empty();
    }

    public Optional<Author> findAuthorByAuthorId(Long authorId) {
        return Optional.ofNullable(inMemoryCache.get(authorId));
    }
}
