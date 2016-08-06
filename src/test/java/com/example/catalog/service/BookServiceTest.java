package com.example.catalog.service;

import com.example.catalog.domain.Book;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.web.client.MockServerRestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
public class BookServiceTest {

    private BookService service;

    @Before
    public void setUp() throws Exception {
        MockServerRestTemplateCustomizer customizer = new MockServerRestTemplateCustomizer();
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder(customizer);
        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.setMessageConverters(Collections.singletonList(new MappingJackson2HttpMessageConverter()));
        service = new BookService(restTemplate);

        customizer.getServer().expect(requestTo("http://localhost:8082/books/1"))
                .andRespond(withSuccess("{\"id\": 1, \"name\": \"TEST BOOK\"}", MediaType.APPLICATION_JSON));
    }

    @Test
    public void getBookDetailsWhenResultIsSuccess()
            throws Exception {
        Book book = this.service.getBookByBookId(1L);
        Assertions.assertThat(book.getId()).isEqualTo(1L);
        Assertions.assertThat(book.getName()).isEqualTo("TEST BOOK");
    }
}