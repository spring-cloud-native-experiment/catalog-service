package com.example.author.repository;

import com.example.author.domain.Author;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AuthorRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void findsAuthorById() throws Exception {
        String authorName = "Erich Gamma";
        Author author = Author.builder().name(authorName).build();
        Long authorId = this.testEntityManager.persistAndGetId(author, Long.class);

        Optional<Author> savedAuthor = authorRepository.findById(authorId);

        assertThat(savedAuthor.isPresent()).isTrue();
        assertThat(savedAuthor.get().getId()).isEqualTo(authorId);
        assertThat(savedAuthor.get().getName()).isEqualTo(authorName);
    }
}