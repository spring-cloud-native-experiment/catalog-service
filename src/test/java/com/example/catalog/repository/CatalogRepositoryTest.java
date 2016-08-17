package com.example.catalog.repository;

import com.example.catalog.domain.Catalog;
import com.example.catalog.domain.CatalogBooks;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class CatalogRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CatalogRepository catalogRepository;

    @Test
    public void findsCatalogById() throws Exception {
        String catalogName = "Literature";
        Catalog catalog = Catalog.builder().name(catalogName).build();
        Long catalogId = this.testEntityManager.persistAndGetId(catalog, Long.class);

        Optional<Catalog> savedCatalog = catalogRepository.findById(catalogId);

        assertThat(savedCatalog).hasValueSatisfying(expected -> {
            assertThat(expected).extracting(Catalog::getId).contains(catalogId);
            assertThat(expected).extracting(Catalog::getName).contains(catalogName);
        });
    }

    @Test
    public void findsBooksByCatalog() throws Exception {
        String catalogName = "Literature";
        Catalog catalog = Catalog.builder().name(catalogName).build();
        Long catalogId = this.testEntityManager.persistAndGetId(catalog, Long.class);
        this.testEntityManager.persist(CatalogBooks.builder().bookId(1L).catalogId(catalogId).build());
        this.testEntityManager.persist(CatalogBooks.builder().bookId(2L).catalogId(catalogId).build());

        Stream<CatalogBooks> booksByCatalog = catalogRepository.findBooksByCatalog(catalogId);

        assertThat(booksByCatalog).extracting(CatalogBooks::getBookId).contains(1L, 2L).hasSize(2);
    }
}