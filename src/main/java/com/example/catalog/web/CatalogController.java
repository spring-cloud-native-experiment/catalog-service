package com.example.catalog.web;

import com.example.catalog.domain.Book;
import com.example.catalog.domain.Catalog;
import com.example.catalog.domain.CatalogBooks;
import com.example.catalog.domain.CatalogRequest;
import com.example.catalog.exception.CatalogNotFoundException;
import com.example.catalog.service.BookService;
import com.example.catalog.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/catalog")
class CatalogController {

    private final CatalogService catalogService;
    private final BookService bookService;

    @Autowired
    CatalogController(CatalogService catalogService, BookService bookService) {
        this.catalogService = catalogService;
        this.bookService = bookService;
    }

    @RequestMapping(method = GET)
    List<Catalog> getAllCatalogs() {
        return catalogService.findAllCatalogs();
    }

    @RequestMapping(path = "/{catalogId}", method = GET)
    Catalog getCatalogById(@PathVariable Long catalogId) {
        return catalogService.findCatalogByCatalogId(catalogId)
                .orElseThrow(CatalogNotFoundException::new);
    }

    @RequestMapping(path = "/{catalogId}/books", method = GET)
    List<Book> getBooksForCatalog(@PathVariable Long catalogId) {
        return catalogService.findCatalogByCatalogId(catalogId)
                .map(Catalog::getId)
                .map(it ->
                        catalogService.findBookIdsByCatalogId(it)
                                .map(CatalogBooks::getBookId)
                                .map(bookService::getBookByBookId))
                .orElseThrow(CatalogNotFoundException::new)
                .collect(Collectors.toList());
    }

    @RequestMapping(method = POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Catalog save(@RequestBody CatalogRequest catalogRequest) {
        return catalogService.save(catalogRequest);
    }
}
