package com.example.catalog.repository;

import com.example.catalog.domain.Catalog;
import com.example.catalog.domain.CatalogBooks;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
@RefreshScope
public interface CatalogRepository extends JpaRepository<Catalog, Long> {

    Optional<Catalog> findById(@Param("id") Long id);

    Optional<Catalog> findByName(@Param("name") String name);

    @Query("SELECT book from Catalog catalog INNER JOIN catalog.books book WHERE catalog.id = :id")
    Stream<CatalogBooks> findBooksByCatalog(@Param("id") Long id);
}
