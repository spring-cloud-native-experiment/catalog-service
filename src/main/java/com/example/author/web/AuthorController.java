package com.example.author.web;

import com.example.author.domain.Author;
import com.example.author.exception.AuthorNotFoundException;
import com.example.author.service.AuthorService;
import com.example.author.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/authors")
class AuthorController {

    private final AuthorService authorService;
    private final BookService bookService;

    @Autowired
    AuthorController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @RequestMapping(method = GET)
    List<Author> getAllAuthors() {
        return authorService.findAllAuthors();
    }

    @RequestMapping(path = "/{authorId}", method = GET)
    Author getAuthorById(@PathVariable Long authorId) {
        return authorService.findAuthorByAuthorId(authorId)
                .orElseThrow(AuthorNotFoundException::new);
    }

    @RequestMapping(path = "/{authorId}/books", method = GET)
    List<Long> getBooksForAuthor(@PathVariable Long authorId) {
        return authorService.findAuthorByAuthorId(authorId)
                .map(Author::getId)
                .map(bookService::getBookIdsForAuthorId)
                .orElseThrow(AuthorNotFoundException::new);
    }
}
