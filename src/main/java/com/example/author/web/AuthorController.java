package com.example.author.web;

import com.example.author.domain.Author;
import com.example.author.exception.AuthorNotFoundException;
import com.example.author.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/authors")
class AuthorController {

    private final AuthorService authorService;

    @Autowired
    AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @RequestMapping(method = GET)
    List<Author> getAllAuthors() {
        return authorService.findAllAuthors()
                .collect(toList());
    }

    @RequestMapping(path = "/{authorId}", method = GET)
    Author getAuthorById(@PathVariable Long authorId) {
        return authorService.findAuthorByAuthorId(authorId)
                .orElseThrow(AuthorNotFoundException::new);
    }
}
