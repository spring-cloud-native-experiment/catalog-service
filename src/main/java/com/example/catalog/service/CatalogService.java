package com.example.catalog.service;

import com.example.catalog.domain.Catalog;
import com.example.catalog.domain.CatalogBooks;
import com.example.catalog.repository.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class CatalogService {

    private CatalogRepository repository;

    @Autowired
    public CatalogService(CatalogRepository repository) {
        this.repository = repository;
    }

    public List<Catalog> findAllCatalogs() {
        return repository.findAll();
    }

    public Optional<Catalog> findCatalogByCatalogId(Long catalogId) {
        return repository.findById(catalogId);
    }

    public Stream<CatalogBooks> findBookIdsByCatalogId(Long catalogId) {
        return repository.findBooksByCatalog(catalogId);
    }
}
