package com.example.author.web;

import com.example.author.exception.AuthorNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AuthorIntegrationTest {
    @LocalServerPort
    private int localServerPort;

    @Test
    public void findsAllAuthors() throws Exception {
        given().
                port(localServerPort).
        when().
                get("/authors").
        then().
                statusCode(200)
        ;
    }

    @Test
    public void findsAuthorById() throws Exception {
        given().port(localServerPort).
                pathParam("id", 123).
        when().
                get("/authors/{id}").
        then().
                statusCode(404).
                body(
                        "message", is("No such author"),
                        "exception", is(AuthorNotFoundException.class.getName())
                )
        ;
    }
}