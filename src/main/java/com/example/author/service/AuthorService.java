package com.example.author.service;

import com.example.author.domain.Author;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
public class AuthorService {
    public Stream<Author> findAllAuthors() {
        return Stream.empty();
    }

    public Optional<Author> findAuthorByAuthorId(Long authorId) {
        return Optional.empty();
    }
}
