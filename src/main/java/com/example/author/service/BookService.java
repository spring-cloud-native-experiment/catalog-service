package com.example.author.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class BookService {
    public List<Long> getBookIdsForAuthorId(Long authorId) {
        return Collections.emptyList();
    }
}
