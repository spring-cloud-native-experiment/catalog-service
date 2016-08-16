package com.example.catalog.service;

import com.example.catalog.domain.Book;
import com.example.catalog.domain.Catalog;
import com.example.catalog.domain.CatalogBooks;
import com.example.catalog.domain.CatalogRequest;
import com.example.catalog.repository.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class CatalogService {

    private CatalogRepository repository;
    private BookService bookService;

    @Autowired
    public CatalogService(CatalogRepository repository, BookService bookService) {
        this.repository = repository;
        this.bookService = bookService;
    }

    public List<Catalog> findAllCatalogs() {
        return repository.findAll();
    }

    public Optional<Catalog> findCatalogByCatalogId(Long catalogId) {
        return repository.findById(catalogId);
    }

    public Optional<Catalog> findCatalogByName(String name) {
        return repository.findByName(name);
    }

    public Stream<CatalogBooks> findBookIdsByCatalogId(Long catalogId) {
        return repository.findBooksByCatalog(catalogId);
    }

    public void save(CatalogRequest catalogRequest) {
        Catalog catalog = findCatalogByName(catalogRequest.getName())
                .orElse(Catalog.builder().name(catalogRequest.getName()).build());
        repository.save(catalog);

        Book book = bookService.getBookByBookId(catalogRequest.getBook());
        catalog.addBook(new CatalogBooks(null, catalog.getId(), book.getId()));
        repository.save(catalog);
    }
}
