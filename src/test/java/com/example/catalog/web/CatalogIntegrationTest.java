package com.example.catalog.web;

import com.example.catalog.domain.Book;
import com.example.catalog.domain.Catalog;
import com.example.catalog.domain.CatalogBooks;
import com.example.catalog.exception.CatalogNotFoundException;
import com.example.catalog.repository.CatalogRepository;
import com.example.catalog.service.BookService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.Arrays;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CatalogIntegrationTest {

    @LocalServerPort
    private int localServerPort;

    @MockBean
    private BookService bookService;

    @Autowired
    private CatalogRepository repository;

    @Before
    public void setUp() throws Exception {
        Catalog catalog = Catalog.builder().name("Literature").build();
        repository.save(catalog);
        catalog.setBooks(Arrays.asList(CatalogBooks.builder().bookId(1L).catalogId(catalog.getId()).build(),
                CatalogBooks.builder().bookId(2L).catalogId(catalog.getId()).build()));
        repository.save(catalog);

        catalog = Catalog.builder().name("Science").build();
        repository.save(catalog);
        catalog.setBooks(Arrays.asList(CatalogBooks.builder().bookId(3L).catalogId(catalog.getId()).build(),
                CatalogBooks.builder().bookId(4L).catalogId(catalog.getId()).build()));
        repository.save(catalog);
    }

    @Test
    public void findsAllCatalogs() throws Exception {
        given().
                port(localServerPort).
        when().
                get("/catalog").
        then().
                statusCode(200).
                body(
                        "[0].name", is("Literature"),
                        "[0].id", is(1),
                        "[1].name", is("Science"),
                        "[1].id", is(2)
                )
        ;
    }

    @Test
    public void findsCatalogById() throws Exception {
        given().
                port(localServerPort).
                pathParam("id", 1).
        when().
                get("/catalog/{id}").
        then().
                statusCode(is(200)).
                body(
                        "id", is(1),
                        "name", is("Literature")
                )
        ;
    }

    @Test
    public void shouldThrow404IfAuthorNotFound() throws Exception {
        given().
                port(localServerPort).
                pathParam("id", 123).
        when().
                get("/catalog/{id}").
        then().
                statusCode(is(404)).
                body(
                        "message", is("No such catalog"),
                        "exception", is(CatalogNotFoundException.class.getName())
                )
        ;
    }

    @Test
    public void findBooksForGivenCatalog() throws Exception {

        BDDMockito.given(this.bookService.getBookByBookId(1L))
                .willReturn(Book.builder().name("TEST BOOK").id(1L).build());
        BDDMockito.given(this.bookService.getBookByBookId(2L))
                .willReturn(Book.builder().name("TEST BOOK1").id(2L).build());


        given().
                port(localServerPort).
                pathParam("id", 1).
        when().
                get("/catalog/{id}/books").
        then().
                statusCode(is(200)).
                log().all().
                body(
                        "[0].id", is(1),
                        "[0].name", is("TEST BOOK"),
                        "[0].id", is(1),
                        "[0].name", is("TEST BOOK")
                )
        ;
    }
}