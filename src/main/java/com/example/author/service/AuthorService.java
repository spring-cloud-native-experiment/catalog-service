package com.example.author.service;

import com.example.author.repository.AuthorRepository;
import com.example.author.domain.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Service
public class AuthorService {

    private AuthorRepository repository;

    @Autowired
    public AuthorService(AuthorRepository repository) {
        this.repository = repository;
    }

    public List<Author> findAllAuthors() {
        return repository.findAll();
    }

    public Optional<Author> findAuthorByAuthorId(Long authorId) {
        return repository.findById(authorId);
    }
}
