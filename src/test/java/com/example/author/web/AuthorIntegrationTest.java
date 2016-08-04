package com.example.author.web;

import com.example.author.exception.AuthorNotFoundException;
import com.example.author.service.BookService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AuthorIntegrationTest {

    @LocalServerPort
    private int localServerPort;

    @MockBean
    private BookService bookService;

    @Test
    @Sql(scripts = "/integrationTestData.sql")
    @Sql(scripts = "/cleanUp.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findsAllAuthors() throws Exception {
        given().
                port(localServerPort).
        when().
                get("/authors").
        then().
                statusCode(200).
                body(
                        "[0].name", is("John"),
                        "[0].id", is(1),
                        "[1].name", is("Doe"),
                        "[1].id", is(2)
                )
        ;
    }

    @Test
    @Sql(scripts = "/integrationTestData.sql")
    @Sql(scripts = "/cleanUp.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findsAuthorById() throws Exception {
        given().
                port(localServerPort).
                pathParam("id", 1).
        when().
                get("/authors/{id}").
        then().
                statusCode(is(200)).
                body(
                        "id", is(1),
                        "name", is("John")
                )
        ;
    }

    @Test
    public void shouldThrow404IfAuthorNotFound() throws Exception {
        given().
                port(localServerPort).
                pathParam("id", 123).
        when().
                get("/authors/{id}").
        then().
                statusCode(is(404)).
                body(
                        "message", is("No such author"),
                        "exception", is(AuthorNotFoundException.class.getName())
                )
        ;
    }

    @Test
    @Sql(scripts = "/integrationTestData.sql")
    @Sql(scripts = "/cleanUp.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findBooksForGivenAuthor() throws Exception {

        BDDMockito.given(this.bookService.getBookIdsForAuthorId(1L))
                .willReturn(Collections.singletonList(1L));

        given().
                port(localServerPort).
                pathParam("id", 1).
        when().
                get("/authors/{id}/books").
        then().
                statusCode(is(200)).
                log().all().
                body("[0]", is(1))
        ;
    }
}